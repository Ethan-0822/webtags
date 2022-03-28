package yh.domain;

import lombok.Data;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @projectName: webtags
 * @package: yh.domain
 * @className: User
 * @author: Ethan
 * @description: TODO
 * @version: 1.0
 */
@Data
@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String openId;

    private String username;
    private String avatar;

    private LocalDateTime lasted;
    private LocalDateTime created;

}