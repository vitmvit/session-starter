package by.vitikova.session.config;

import by.vitikova.session.client.SessionClient;
import by.vitikova.session.processor.SessionManagementBeanPostProcessor;
import by.vitikova.session.property.SessionManagementProperty;
import by.vitikova.session.provider.impl.DefaultBlackListProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

/**
 * Автоматическая конфигурация для управления сессиями.
 * <p>
 * Этот класс обеспечивает автоматическую настройку компонентов, необходимых
 * для работы с сессиями в приложении. Конфигурация активируется, только если
 * свойство {@code session.manager.enable} установлено в {@code true}.
 */
@EnableAsync
@AutoConfiguration
@RequiredArgsConstructor
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@EnableConfigurationProperties(SessionManagementProperty.class)
@ConditionalOnProperty(value = "session.enable", havingValue = "true")
public class SessionAutoConfiguration {

    @Bean
    public SessionManagementBeanPostProcessor sessionManagerBeanPostProcessor() {
        return new SessionManagementBeanPostProcessor();
    }

    @Bean
    public SessionClient sessionProviderCommunicator(SessionManagementProperty sessionManagerProperties) {
        return new SessionClient(restTemplate(), sessionManagerProperties);
    }

    @Bean
    public DefaultBlackListProvider blackListProvider(SessionManagementProperty sessionManagerProperties) {
        return new DefaultBlackListProvider(sessionManagerProperties);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}