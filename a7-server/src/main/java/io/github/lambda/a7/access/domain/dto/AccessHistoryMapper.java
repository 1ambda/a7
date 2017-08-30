package io.github.lambda.a7.access.domain.dto;

import io.github.lambda.a7.access.domain.AccessHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AccessHistoryMapper {
    AccessHistoryMapper INSTANCE = Mappers.getMapper(AccessHistoryMapper.class);

    @Mappings({
            @Mapping(source = "browserName", target = "browserName"),
            @Mapping(source = "browserVersion", target = "browserVersion"),
            @Mapping(source = "osName", target = "osName"),
            @Mapping(source = "osVersion", target = "osVersion"),
            @Mapping(source = "isMobile", target = "isMobile"),
            @Mapping(source = "timezone", target = "timezone"),
            @Mapping(source = "language", target = "language"),
            @Mapping(source = "userAgent", target = "userAgent"),
    })
    AccessHistoryDTO toDTO(AccessHistory accessHistory);
}
