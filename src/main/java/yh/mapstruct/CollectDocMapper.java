package yh.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import yh.base.dto.CollectDto;
import yh.search.CollectDoc;

/**
 * 2  * @Description :
 * 4
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CollectDocMapper {

    @Mappings({
            @Mapping(source = "userId", target = "user.id"),
            @Mapping(source = "userAvatar", target = "user.avatar"),
            @Mapping(source = "username", target = "user.username")
    })
    CollectDto toDto(CollectDoc collectDoc);
}