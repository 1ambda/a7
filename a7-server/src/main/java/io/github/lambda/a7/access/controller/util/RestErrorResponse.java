package io.github.lambda.a7.access.controller.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Builder
public class RestErrorResponse {
    public Instant timestamp;

    @NotNull
    @JsonIgnore
    public HttpStatus status;

    @Builder.Default
    @NotNull
    public String exception = "";

    @Builder.Default
    @NotNull
    public String message = "";

    @NotNull
    public String path;

    public long getTimestamp() {
        return timestamp.toEpochMilli();
    }

    public int getStatus() {
        return status.value();
    }

    public String getError() {
        return status.getReasonPhrase();
    }

    public static ResponseEntity<RestErrorResponse> createGeneralResponse(String message,
                                                                          String exception,
                                                                          HttpStatus status) {

        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequestUri();
        UriComponents currentUri = builder.build();
        RestErrorResponse error = RestErrorResponse.builder()
                .status(status)
                .path(currentUri.getPath())
                .exception(exception)
                .message(message)
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(error, status);
    }

    public static ResponseEntity<?> createBadRequestResponse(String message) {
        return RestErrorResponse.createGeneralResponse(message,
                IllegalArgumentException.class.getCanonicalName(), HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<?> createNotFoundResponse(String message) {
        return RestErrorResponse.createGeneralResponse(message,
                IllegalArgumentException.class.getCanonicalName(), HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<?> createConflictResponse(String message) {
        return RestErrorResponse.createGeneralResponse(message,
                IllegalStateException.class.getCanonicalName(), HttpStatus.CONFLICT);
    }
}
