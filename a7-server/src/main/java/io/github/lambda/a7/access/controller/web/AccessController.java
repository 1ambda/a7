package io.github.lambda.a7.access.controller.web;

import com.codahale.metrics.annotation.Timed;
import io.github.lambda.a7.access.infrastrucure.StompTopics;
import io.github.lambda.a7.access.controller.util.RestErrorResponse;
import io.github.lambda.a7.access.domain.dto.AccessHistoryDTO;
import io.github.lambda.a7.access.domain.dto.AccessHistoryListDTO;
import io.github.lambda.a7.access.domain.dto.AccessHistoryMapper;
import io.github.lambda.a7.access.domain.dto.PaginationDTO;
import io.github.lambda.a7.access.repository.AccessHistoryRepository;
import io.github.lambda.a7.access.domain.AccessHistory;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/access", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccessController {
    private static final Logger LOG = LoggerFactory.getLogger(AccessController.class);

    @Autowired SimpMessagingTemplate messagingTemplate;
    @Autowired AccessHistoryRepository accessHistoryRepository;

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = AccessHistoryDTO.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad Request", response = RestErrorResponse.class),
    })
    @ResponseStatus(HttpStatus.OK)
    @Timed
    @GetMapping("")
    public ResponseEntity<?> findAll(Pageable pageable) {
        Iterable<AccessHistory> accesses = Collections.EMPTY_LIST;

        LOG.info(pageable.toString());

        try {
            accesses = accessHistoryRepository.findAll(pageable);
        } catch (PropertyReferenceException e) {
            return RestErrorResponse.createBadRequestResponse(e.getMessage());
        }

        Long totalCount = accessHistoryRepository.count();

        List<AccessHistoryDTO> content = StreamSupport.stream(accesses.spliterator(), false)
                .map(accessHistory -> AccessHistoryMapper.INSTANCE.toDTO(accessHistory))
                .collect(Collectors.toList());

        PaginationDTO pagination = PaginationDTO.builder()
                .contentSize(totalCount)
                .pageNumber(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .build();

        AccessHistoryListDTO response = AccessHistoryListDTO.builder()
                .content(content)
                .meta(pagination)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = AccessHistoryDTO.class),
            @ApiResponse(code = 400, message = "Bad Request", response = RestErrorResponse.class),
    })
    @Timed
    @GetMapping("/{id}")
    public ResponseEntity<?> findOneBy(@PathVariable Long id) {
        AccessHistory found = accessHistoryRepository.findOne(id);
        if (found == null) {
            return RestErrorResponse.createBadRequestResponse("Failed to find Access with id: " + id);
        }

        AccessHistoryDTO response = AccessHistoryMapper.INSTANCE.toDTO(found);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(code = 201, message = "CREATE", response = AccessHistoryDTO.class),
    })
    @Timed
    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody AccessHistory accessHistory) {
        accessHistory.setTimestamp(Instant.now().toEpochMilli());

        AccessHistory created = accessHistoryRepository.save(accessHistory);
        AccessHistoryDTO response = AccessHistoryMapper.INSTANCE.toDTO(created);

        Long count = accessHistoryRepository.count();
        this.messagingTemplate.convertAndSend(StompTopics.BROADCAST_ACCESS_COUNT, count);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
