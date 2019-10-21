package edu.mum.life.repository.search;
import edu.mum.life.domain.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Item} entity.
 */
public interface ItemSearchRepository extends ElasticsearchRepository<Item, Long> {
}
