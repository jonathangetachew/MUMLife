package edu.mum.life.repository.search;
import edu.mum.life.domain.Event;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Event} entity.
 */
public interface EventSearchRepository extends ElasticsearchRepository<Event, Long> {
}
