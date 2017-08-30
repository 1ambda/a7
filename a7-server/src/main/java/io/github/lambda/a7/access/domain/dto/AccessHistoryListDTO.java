package io.github.lambda.a7.access.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AccessHistoryListDTO {
    private PaginationDTO meta;

    private List<AccessHistoryDTO> content;
}
