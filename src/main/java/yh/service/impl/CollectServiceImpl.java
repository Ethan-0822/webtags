package yh.service.impl;

import cn.hutool.core.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import yh.base.dto.CollectDto;
import yh.base.dto.SidebarDateDto;
import yh.base.dto.UserDto;
import yh.base.lang.Consts;
import yh.domain.Collect;
import yh.domain.User;
import yh.mapstruct.CollectMapper;
import yh.repository.CollectRepository;
import yh.service.CollectService;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * @projectName: webtags
 * @package: yh.service.impl
 * @className: CollectServiceImpl
 * @author: Ethan
 * @description: TODO
 * @version: 1.0
 */
@Service
public class CollectServiceImpl implements CollectService {
    @Autowired
    CollectRepository collectRepository;

    @Autowired
    CollectMapper collectMapper;

    @Autowired
    HttpSession httpSession;

    /**
     * 用户的收藏日期列表
     */
    @Override
    public List<SidebarDateDto> getSidebarDateDtoByUserId(Long userId) {
        List<Date> collectDateList = collectRepository.getSidebarDateDtoByUserId(userId);
        List<SidebarDateDto> sidebarDateDtos = new ArrayList<>();

        for (Date date : collectDateList) {
            String parent = DateUtil.format(date, "yyyy年MM月");
            String title = DateUtil.format(date, "yyyy年MM月dd日");

            handleDateline(sidebarDateDtos, parent, title);
        }
        return sidebarDateDtos;
    }

    /**
     * 相同月份的日期放到一起
     */
    private List<SidebarDateDto> handleDateline(List<SidebarDateDto> sidebarDateDtos, String parent, String title) {
        SidebarDateDto sidebarDateDto = new SidebarDateDto();
        sidebarDateDto.setTitle(title);

        Optional<SidebarDateDto> optional = sidebarDateDtos
                .stream()
                .filter(vo -> vo.getTitle().equals(parent))
                .findFirst();
        if(optional.isPresent()){
            optional.get().getChildren().add(sidebarDateDto);
        }else{
            //没有找到月份，新建一个月份标题，并加入侧边栏日期集合
            SidebarDateDto parentDto = new SidebarDateDto();
            parentDto.setTitle(parent);

            parentDto.getChildren().add(sidebarDateDto);
            sidebarDateDtos.add(parentDto);
        }

        return sidebarDateDtos;
    }

    /**
     * 查询某用户的收藏
     */
    @Override
    public Page<CollectDto> findUserCollects(long userId, String dateline, Pageable pageable) {

        Page<Collect> page = collectRepository.findAll((root, query, builder) -> {

            // 构建查询
            Predicate predicate = builder.conjunction();

            // 左外连接
            Join<Collect, User> join = root.join("user", JoinType.LEFT);
            predicate.getExpressions().add(builder.equal(join.get("id"), userId));


            // 仅查询某一天的记录
            if (!"all".equals(dateline)) {
                LocalDate localDate = LocalDate.parse(dateline, DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
                predicate.getExpressions().add(
                        builder.equal(root.<Date>get("collected"), localDate));
            }

            // 私有收藏非本人不能看
            UserDto userDto = (UserDto) httpSession.getAttribute(Consts.CURRENT_USER);
            boolean isOwn = userDto != null && userDto.getId() == userId;
            if (!isOwn) {
                predicate.getExpressions().add(builder.equal(root.get("personal"), 0));
            }

            return predicate;
        }, pageable);
        return page.map(collectMapper::toDto);
    }


    @Override
    public Collect findById(Long id) {
        Optional<Collect> optional = collectRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        collectRepository.deleteById(id);

    }

    /**
     * 更新 or 修改
     */
    @Override
    public void save(Collect collect) {
        if (collect.getId() == null) {
            collect.setCreated(LocalDateTime.now());
            collect.setCollected(LocalDate.now());

            collectRepository.save(collect);
        } else {

            Collect temp = collectRepository.getById(collect.getId());

            temp.setTitle(collect.getTitle());
            temp.setUrl(collect.getUrl());
            temp.setNote(collect.getNote());
            temp.setUser(collect.getUser());
            temp.setPersonal(collect.getPersonal());

            temp.setCollected(LocalDate.now());
            collectRepository.save(temp);
        }
    }

    /**
     * 去重日期并倒序查询收藏表
     */
    @Override
    public Page<CollectDto> findSquareCollects(Pageable pageable) {
        Page<Collect> page = collectRepository.findAllByPersonal(0, pageable);
        return page.map(collectMapper::toDto);
    }


}