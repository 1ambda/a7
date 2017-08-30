package io.github.lambda.a7.access.controller.web;

import com.codahale.metrics.annotation.Timed;
import io.github.lambda.a7.access.controller.util.RestErrorResponse;
import io.github.lambda.a7.access.infrastrucure.SessionUtil;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/session", produces = MediaType.APPLICATION_JSON_VALUE)
public class SessionController {
    private static final Logger LOG = LoggerFactory.getLogger(SessionController.class);

    @Autowired SessionUtil sessionUtil;

    @ApiResponses({
            @ApiResponse(code = 201, message = "Created", response = Void.class),
            @ApiResponse(code = 400, message = "Bad Request", response = RestErrorResponse.class),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @Timed
    @PostMapping("/action/create")
    public ResponseEntity<?> auth(HttpSession session) {
        /**
         * If we use `HttpSession` as a argument,
         * spring session will automatically create new session when there is no session
         **/

        /**
         * don't return session id in the response body in production.
         */
        Map<String, String> response = new HashMap();
        if (session != null && !StringUtils.isEmpty(session.getId())) {
            response.put("session", session.getId());
        }

        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @ApiResponses({
        @ApiResponse(code = 200, message = "OK", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = RestErrorResponse.class),
    })
    @ResponseStatus(HttpStatus.OK)
    @Timed
    @DeleteMapping("/action/invalidate")
    public ResponseEntity<?> invalidate(HttpSession session) {
        if (!session.isNew()) {
            /**
             * session.invalidate doesn't publish HttpSessionDestroyedEvent, so need to trigger them.
             */
            sessionUtil.publishHttpSessionDestroyedEvent(session);
        }

        session.invalidate();

        return new ResponseEntity("", HttpStatus.OK);
    }
}
