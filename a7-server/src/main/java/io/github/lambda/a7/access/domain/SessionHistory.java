package io.github.lambda.a7.access.domain;

import io.github.lambda.a7.access.domain.common.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "session_history")
public class SessionHistory extends BaseEntity {
    @NotNull
    @NonNull
    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @NotNull
    @NonNull
    @Column(name = "session_creation_time", nullable = false)
    private Long sessionCreationTime;
}
