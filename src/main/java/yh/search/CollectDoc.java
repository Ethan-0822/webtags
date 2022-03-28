package yh.search;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @projectName: webtags
 * @package: yh.search
 * @className: CollectDoc
 * @author: Ethan
 * @description: 收藏实体类对应的ES索引
 * @version: 1.0
 */

@Data
@Document(indexName = "yh_collect", createIndex = true)
public class CollectDoc {

    @Id
    private Long id;

    @Field(type = FieldType.Text, searchAnalyzer = "ik_smart", analyzer = "ik_max_word")
    private String title;

    @Field(type = FieldType.Text, searchAnalyzer = "ik_smart", analyzer = "ik_max_word")
    private String note;

    @Field(type = FieldType.Text)
    private String url;

    @Field(type = FieldType.Integer)
    private Integer personal;

    @Field(type = FieldType.Date)
    private LocalDate collected;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ssz")
    private LocalDateTime created;

    @Field(name = "userId", type = FieldType.Long)
    private long userId;

    @Field(type = FieldType.Keyword)
    private String username;

    @Field(name = "userAvatar", type = FieldType.Text)
    private String userAvatar;

}