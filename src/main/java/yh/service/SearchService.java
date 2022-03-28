package yh.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import yh.base.dto.CollectDto;

/**
 * 2  * @Description :
 * 4
 */
public interface SearchService {
    Page<CollectDto> search(String keyword, Long userId, Pageable pageable);
}
