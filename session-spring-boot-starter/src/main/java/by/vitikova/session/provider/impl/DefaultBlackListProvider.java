package by.vitikova.session.provider.impl;

import by.vitikova.session.property.SessionManagementProperty;
import by.vitikova.session.provider.BlackListProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * Реализация провайдера черного списка по умолчанию.
 * <p>
 * Данный класс реализует интерфейс {@link BlackListProvider} и предоставляет
 * доступ к черному списку значений, которые не допускаются в систему. Черный
 * список управляется через свойства {@link SessionManagementProperty}.
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultBlackListProvider implements BlackListProvider {

    private final SessionManagementProperty sessionManagerProperties;

    @Override
    public Set<String> getBlackList() {
        log.info("DefaultBlackListProvider: getBlackList - " + sessionManagerProperties.getBlackList());
        return sessionManagerProperties.getBlackList();
    }
}