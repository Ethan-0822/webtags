package yh.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 通过继承jpa的ElasticsearchRepository接口完成ES常用操作，如crud等
 */
public interface CollectDocRepository extends ElasticsearchRepository<CollectDoc, Long> {
}
