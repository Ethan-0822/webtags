package yh.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import yh.base.dto.CollectDto;
import yh.base.dto.SidebarDateDto;
import yh.domain.Collect;

import java.util.List;

/**
 * 2  * @Description :
 * 4
 */
public interface CollectService {

    List<SidebarDateDto> getSidebarDateDtoByUserId(Long userId);

    Page<CollectDto> findUserCollects(long userId, String dateline, Pageable pageable);

    Collect findById(Long id);

    void deleteById(Long id);

    void save(Collect collect);

    Page<CollectDto> findSquareCollects(Pageable pageable);
}
