package io.github.lambda.a7.access.repository;

import io.github.lambda.a7.access.domain.AccessHistory;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AccessHistoryRepository extends PagingAndSortingRepository<AccessHistory, Long> {
    Long countByTimestampGreaterThanEqualAndTimestampLessThan(Long from, Long until);
}
