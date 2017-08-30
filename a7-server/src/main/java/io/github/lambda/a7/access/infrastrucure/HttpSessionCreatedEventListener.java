package io.github.lambda.a7.access.infrastrucure;

import io.github.lambda.a7.access.configuration.SessionConfig;
import io.github.lambda.a7.access.domain.SessionHistory;
import io.github.lambda.a7.access.repository.SessionHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.stereotype.Component;

@Component
public class HttpSessionCreatedEventListener implements ApplicationListener<HttpSessionCreatedEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(HttpSessionCreatedEventListener.class);

    @Autowired SimpMessagingTemplate messagingTemplate;
    @Autowired SessionHistoryRepository sessionHistoryRepository;
    @Autowired SessionUtil sessionUtil;

    @Override
    public void onApplicationEvent(HttpSessionCreatedEvent event) {
        LOG.info("Session Created: " + event.getSession().getId());
        SessionHistory sessionHistory = SessionHistory.builder()
            .sessionId(event.getSession().getId())
            .sessionCreationTime(event.getSession().getCreationTime())
            .build();
        sessionHistoryRepository.save(sessionHistory);

        int count = sessionUtil.getSessionCountFromRedis();
        this.messagingTemplate.convertAndSend(StompTopics.BROADCAST_SESSION_COUNT, count);
    }
}
