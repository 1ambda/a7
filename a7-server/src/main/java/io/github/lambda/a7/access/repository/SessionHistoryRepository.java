package io.github.lambda.a7.access.repository;

import io.github.lambda.a7.access.domain.SessionHistory;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SessionHistoryRepository extends PagingAndSortingRepository<SessionHistory, Long> {
}
