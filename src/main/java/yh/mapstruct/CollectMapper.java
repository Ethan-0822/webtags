package yh.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import yh.base.dto.CollectDto;
import yh.domain.Collect;

/**
 * 2  * @Description :
 * 4
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CollectMapper {
    CollectDto toDto(Collect collect);
}
