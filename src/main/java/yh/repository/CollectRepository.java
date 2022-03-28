package yh.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import yh.domain.Collect;

import java.util.Date;
import java.util.List;

/**
 * 2  * @Description :
 * 4
 */
public interface CollectRepository  extends JpaRepository<Collect, Long>, JpaSpecificationExecutor<Collect> {

    /**
     * 去重日期并倒序查询收藏表
     * @param userId 用户id
     * @return 日期列表
     */
    @Query(value = "select distinct collected from collect where user_id = ? order by collected desc", nativeQuery = true)
    List<Date> getSidebarDateDtoByUserId(long userId);

    Page<Collect> findAllByPersonal(Integer personal, Pageable pageable);
}
