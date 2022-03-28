package yh.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;
import yh.base.dto.CollectDto;
import yh.base.dto.UserDto;
import yh.base.lang.Consts;
import yh.mapstruct.CollectDocMapper;
import yh.search.CollectDoc;
import yh.service.SearchService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @projectName: webtags
 * @package: yh.service.impl
 * @className: SearchServiceImpl
 * @author: Ethan
 * @description: TODO
 * @version: 1.0
 */
@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    HttpSession httpSession;

    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    CollectDocMapper collectDocMapper;


    /**
     * ES搜索文档
     * 对于已经登录用户，可以搜索公开和私有
     * 对于未登录用户，只能搜索公开
     */
    @Override
    public Page<CollectDto> search(String keyword, Long userId, Pageable pageable) {

        Criteria criteria = new Criteria();

        if (userId != null && userId > 0) {
            // 添加userId的条件查询
            criteria.and(new Criteria("userId").is(userId));
        }

        UserDto userDto = (UserDto) httpSession.getAttribute(Consts.CURRENT_USER);

        if(userDto != null && userId != null){
            // 已登录用户
            boolean isOwn = userId.longValue() == userDto.getId().longValue();
            if(isOwn){

                criteria.and(new Criteria("personal").in(0,1));
            }else{

                criteria.and(new Criteria("personal").in(0));
            }
        }else{
            // 未登录用户
            criteria.and(new Criteria("personal").is(0));
        }

        // 对标题与笔记内容进行关键词搜索，两者关系为or
        criteria.and(new Criteria("title").matches(keyword))
                .or(new Criteria("note").matches(keyword));

        CriteriaQuery criteriaQuery = new CriteriaQuery(criteria).setPageable(pageable);
        SearchHits<CollectDoc> searchHits = elasticsearchRestTemplate.search(criteriaQuery, CollectDoc.class);

        List<CollectDoc> collectDocs = searchHits
                .get()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        Page<CollectDoc> docPage = new PageImpl<>(collectDocs, pageable, searchHits.getTotalHits());
        log.info("共查出 {} 条记录", docPage.getTotalElements());

        return docPage.map(collectDocMapper::toDto);
    }
}