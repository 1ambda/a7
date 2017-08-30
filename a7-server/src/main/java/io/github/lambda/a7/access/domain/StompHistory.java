package io.github.lambda.a7.access.domain;

import io.github.lambda.a7.access.domain.common.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "stomp_history")
public class StompHistory extends BaseEntity {
    @NotNull
    @NonNull
    @Column(name = "native_session_id", nullable = false)
    private String native_session_id;

    @NotNull
    @NonNull
    @Column(name = "simp_session_id", nullable = false)
    private String simp_session_id;
}
