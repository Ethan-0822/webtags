package yh.base.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @projectName: webtags
 * @package: yh.base.Dto
 * @className: CollectDto
 * @author: Ethan
 * @description: TODO
 * @version: 1.0
 */

@Data
public class CollectDto implements Serializable {

    private Long id;

    private String title;
    private String url;
    private String note;

    private UserDto user;

    private Integer personal = 0;

    private LocalDate collected;
    private LocalDateTime created;
}