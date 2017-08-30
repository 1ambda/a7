package io.github.lambda.a7.access.repository;

import io.github.lambda.a7.access.domain.StompHistory;
import org.springframework.data.repository.CrudRepository;

public interface StompHistoryRepository extends CrudRepository<StompHistory, Long> {
}
