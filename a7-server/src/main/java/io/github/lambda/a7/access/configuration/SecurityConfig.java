package io.github.lambda.a7.access.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity(debug = false /** disable in production */)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(SecurityConfig.class);

    // TODO: <ExpiringSession> requires spring-session 1.4.0+
    @Autowired FindByIndexNameSessionRepository sessionRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable();
        http.requestCache().requestCache(new NullRequestCache());
        http.headers().frameOptions().sameOrigin();

        http.authorizeRequests()
            .anyRequest().anonymous();

        http.sessionManagement()
            .invalidSessionStrategy((request, response) -> {
                response.setHeader("Set-Cookie", "");
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid Session");
            })
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .maximumSessions(1)
            .sessionRegistry(sessionRegistry());
    }

    @Bean
    @SuppressWarnings("unchecked")
    public SpringSessionBackedSessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry(this.sessionRepository);
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
