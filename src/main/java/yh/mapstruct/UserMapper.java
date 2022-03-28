package yh.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import yh.base.dto.UserDto;
import yh.domain.User;
/**
 * 2  * @Description :
 * 4
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDto toDto(User user);
}
