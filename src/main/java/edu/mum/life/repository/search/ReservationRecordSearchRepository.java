package edu.mum.life.repository.search;
import edu.mum.life.domain.ReservationRecord;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ReservationRecord} entity.
 */
public interface ReservationRecordSearchRepository extends ElasticsearchRepository<ReservationRecord, Long> {
}
