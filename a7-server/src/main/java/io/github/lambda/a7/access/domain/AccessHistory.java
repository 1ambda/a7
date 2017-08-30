package io.github.lambda.a7.access.domain;

import io.github.lambda.a7.access.domain.common.BaseEntity;
import io.github.lambda.a7.access.domain.common.YesNo;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "access_history")
public class AccessHistory extends BaseEntity {
    @NotNull @NonNull
    @Column(name = "browser_name", nullable = false)
    private String browserName;

    @NotNull @NonNull
    @Column(name = "browser_version", nullable = false)
    private String browserVersion;

    @NotNull @NonNull
    @Column(name = "os_name", nullable = false)
    private String osName;

    @NotNull @NonNull
    @Column(name = "os_version", nullable = false)
    private String osVersion;

    @NotNull @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "is_mobile", nullable = false)
    private YesNo isMobile;

    @NotNull @NonNull
    @Column(name = "timezone", nullable = false)
    private String timezone;

    @Setter @NonNull
    @Column(name = "timestamp", nullable = false)
    private Long timestamp;

    @NotNull @NonNull
    @Column(name = "language", nullable = false)
    private String language;

    @NotNull @NonNull
    @Column(name = "user_agent", nullable = false, columnDefinition = "TEXT")
    private String userAgent;
}
