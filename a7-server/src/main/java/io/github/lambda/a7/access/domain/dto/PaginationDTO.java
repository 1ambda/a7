package io.github.lambda.a7.access.domain.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class PaginationDTO {
    @NotNull
    private Long contentSize;

    @NotNull
    private int pageSize;

    @NotNull
    private int pageNumber;
}
