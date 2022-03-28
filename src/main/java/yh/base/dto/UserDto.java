package yh.base.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @projectName: webtags
 * @package: yh.base.Dto
 * @className: UserDto
 * @author: Ethan
 * @description: TODO
 * @version: 1.0
 */
@Data
public class UserDto implements Serializable {
    private Long id;

    private String username;
    private String avatar;

    private LocalDateTime lasted;
    private LocalDateTime created;
}