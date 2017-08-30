package io.github.lambda.a7.access.domain.dto;

import io.github.lambda.a7.access.domain.common.YesNo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Data
public class AccessHistoryDTO {
    @ApiModelProperty(required = true)
    @NotNull
    private String browserName;

    @ApiModelProperty(required = true)
    @NotNull
    private String browserVersion;

    @ApiModelProperty(required = false)
    @NotNull
    private String osName;

    @ApiModelProperty(required = false)
    @NotNull
    private String osVersion;

    @ApiModelProperty(required = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private YesNo isMobile;

    @ApiModelProperty(required = false)
    @NotNull
    private String timezone;

    @ApiModelProperty(example = "server measured timestamp (GMT 0)", required = true)
    @NotNull
    private Long timestamp;

    @ApiModelProperty(required = false)
    @NotNull
    private String language;

    @ApiModelProperty(example = "browser user agent value", required = true)
    @NotNull
    private String userAgent;
}
