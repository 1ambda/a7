package io.github.lambda.a7.access.infrastrucure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.web.context.support.SecurityWebApplicationContextUtils;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

@Component
public class SessionUtil {
    @Qualifier("sessionRedisTemplate")
    @Autowired RedisTemplate<Object, Object> redisTemplate;
    public static final String REDIS_SESSION_KEY = "spring:session:sessions:[^(expire)]*";
    public static final String REDIS_SESSION_PREFIX = "spring:session:sessions:";

    public void publishHttpSessionDestroyedEvent(HttpSession session) {
        HttpSessionDestroyedEvent e = new HttpSessionDestroyedEvent(session);
        ServletContext context = session.getServletContext();
        SecurityWebApplicationContextUtils.findRequiredWebApplicationContext(context).publishEvent(e);
    }

    public int getSessionCountFromRedis() {
        return redisTemplate.keys(SessionUtil.REDIS_SESSION_KEY).size();
    }
}
