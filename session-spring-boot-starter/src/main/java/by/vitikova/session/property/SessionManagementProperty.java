package by.vitikova.session.property;

import by.vitikova.session.provider.BlackListProvider;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс, представляющий свойства менеджера сессий.
 * <p>
 * Данный класс используется для привязки свойств конфигурации из файлов
 * свойств (application.yml) с префиксом {@code session}.
 * Он содержит флаг активности стартера, URL провайдера, черный список и набор классов провайдеров черного списка.
 */
@Data
@ConfigurationProperties(prefix = "session")
public class SessionManagementProperty {

    private Boolean enable;
    private String sessionProviderUrl;
    private final Set<String> blackList = new HashSet<>();
    private final Set<Class<? extends BlackListProvider>> blackListProviders = new HashSet<>();
}