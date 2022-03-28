package yh.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @projectName: webtags
 * @package: yh.domain
 * @className: Collect
 * @author: Ethan
 * @description: TODO
 * @version: 1.0
 */
@Data
@Entity
@Table(name = "collect")
public class Collect implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String url;
    private String note;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //  是否公开，0公开，1私有，默认公开
    private Integer personal = 0;

    private LocalDate collected;
    private LocalDateTime created;
}
