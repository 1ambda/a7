package io.github.lambda.a7.access.infrastrucure;

import io.github.lambda.a7.access.domain.StompHistory;
import io.github.lambda.a7.access.repository.StompHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitManagementTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.util.stream.Collectors;

@Component
public class StompConnectedEventListener implements ApplicationListener<SessionConnectedEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(StompConnectedEventListener.class);

    @Autowired SimpMessagingTemplate messagingTemplate;
    @Autowired StompHistoryRepository stompHistoryRepository;
    @Autowired RabbitManagementTemplate rabbitManagementTemplate;

    @Override
    public void onApplicationEvent(SessionConnectedEvent event) {
        StompHeaderAccessor header = StompHeaderAccessor.wrap(event.getMessage());
        StompHistory history = StompHistory.builder()
            .native_session_id(header.getNativeHeader("session").get(0))
            .simp_session_id(header.getSessionId())
            .build();
        stompHistoryRepository.save(history);

        int count = rabbitManagementTemplate.getBindings().stream()
            .filter(b -> b.getRoutingKey().equalsIgnoreCase(StompTopics.AMQP_WEBSOCKET_SUBSCRIPTION))
            .collect(Collectors.toList())
            .size();
        /** at this point, subscription has not been registered in rabbit, thus need to + 1 */
        count = count + 1;
        messagingTemplate.convertAndSend(StompTopics.BROADCAST_WEBSOCKET_COUNT, count);
    }
}
