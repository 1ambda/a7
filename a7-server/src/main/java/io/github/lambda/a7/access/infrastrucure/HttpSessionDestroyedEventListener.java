package io.github.lambda.a7.access.infrastrucure;

import io.github.lambda.a7.access.repository.SessionHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

@Component
public class HttpSessionDestroyedEventListener implements ApplicationListener<HttpSessionDestroyedEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(HttpSessionDestroyedEventListener.class);

    @Autowired SimpMessagingTemplate messagingTemplate;
    @Autowired SessionHistoryRepository sessionHistoryRepository;
    @Autowired SessionUtil sessionUtil;

    @Override
    public void onApplicationEvent(HttpSessionDestroyedEvent event) {
        LOG.info("Session Destroy: " + event.getSession().getId());
        int count = sessionUtil.getSessionCountFromRedis();
        this.messagingTemplate.convertAndSend(StompTopics.BROADCAST_SESSION_COUNT, count);
    }
}
