package io.github.lambda.a7.access.domain.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;

@NoArgsConstructor()
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false,
            columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    protected Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false,
            columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    protected Instant updatedAt;

    public Long getCreatedAt() {
        return createdAt == null ? null : createdAt.toEpochMilli();
    }

    public Long getUpdatedAt() {
        return updatedAt == null ? null : updatedAt.toEpochMilli();
    }
}
