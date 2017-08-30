package io.github.lambda.a7.access.configuration;

import org.springframework.amqp.rabbit.core.RabbitManagementTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class RabbitConfig {
    @Value("${spring.rabbitmq.host}")
    private String RABBIT_HOST;

    @Value("${spring.rabbitmq.username}")
    private String RABBIT_USERNAME;

    @Value("${spring.rabbitmq.password}")
    private String RABBIT_PASSWORD;

    private final int RABBIT_MANAGEMENT_PORT = 15672;

    @Bean RabbitManagementTemplate getRabbitManagementTemplate() throws URISyntaxException {
        String url = new URI("http://localhost:" + RABBIT_MANAGEMENT_PORT + "/api/").toString();
        return new RabbitManagementTemplate(url, RABBIT_USERNAME, RABBIT_PASSWORD);
    }
}
