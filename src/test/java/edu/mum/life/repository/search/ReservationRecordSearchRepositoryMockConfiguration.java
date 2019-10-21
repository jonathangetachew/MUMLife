package edu.mum.life.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ReservationRecordSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ReservationRecordSearchRepositoryMockConfiguration {

    @MockBean
    private ReservationRecordSearchRepository mockReservationRecordSearchRepository;

}
