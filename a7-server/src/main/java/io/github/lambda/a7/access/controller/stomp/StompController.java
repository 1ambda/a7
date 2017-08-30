package io.github.lambda.a7.access.controller.stomp;

import com.codahale.metrics.annotation.Timed;
import io.github.lambda.a7.access.infrastrucure.SessionUtil;
import io.github.lambda.a7.access.infrastrucure.StompTopics;
import io.github.lambda.a7.access.repository.AccessHistoryRepository;
import io.github.lambda.a7.access.repository.StompHistoryRepository;
import org.springframework.amqp.rabbit.core.RabbitManagementTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.stream.Collectors;

@Controller
public class StompController {
    @Autowired AccessHistoryRepository accessHistoryRepository;
    @Autowired
    StompHistoryRepository stompHistoryRepository;
    @Autowired RabbitManagementTemplate rabbitManagementTemplate;
    @Autowired SessionUtil sessionUtil;

    @Timed
    @SubscribeMapping(StompTopics.UNICAST_ACCESS_COUNT)
    public Long getAccessHistoryCount() {
        return accessHistoryRepository.count();
    }

    @Timed
    @SubscribeMapping(StompTopics.UNICAST_WEBSOCKET_COUNT)
    public int getWebsocketConnectionCount() {
        int count = rabbitManagementTemplate.getBindings().stream()
            .filter(b -> b.getRoutingKey().equalsIgnoreCase(StompTopics.AMQP_WEBSOCKET_SUBSCRIPTION))
            .collect(Collectors.toList())
            .size();

        return count;
    }

    @Timed
    @SubscribeMapping(StompTopics.UNICAST_SESSION_COUNT)
    public int getSessionConnectionCount() {
        return sessionUtil.getSessionCountFromRedis();
    }
}
