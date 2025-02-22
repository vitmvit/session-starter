package by.vitikova.session.client;

import by.vitikova.session.SessionCreateDto;
import by.vitikova.session.SessionDto;
import by.vitikova.session.exception.SessionServiceException;
import by.vitikova.session.property.SessionManagementProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Класс, представляющий клиент для работы с сессиями.
 * <p>
 * Этот класс использует {@link RestTemplate} для выполнения HTTP-запросов
 * к удаленному серверу для управления сессиями.
 */
@Slf4j
@RequiredArgsConstructor
public class SessionClient {

    private final RestTemplate restTemplate;
    private final SessionManagementProperty sessionManagerProperties;

    /**
     * Получает сессию на основе предоставленной информации для авторизации.
     *
     * @param sessionCreateDto объект, содержащий данные для авторизации, такие как логин
     * @return объект {@link SessionDto}, представляющий информацию о сессии
     * @throws SessionServiceException если происходит ошибка во время запроса к серверу
     */
    public SessionDto getSession(SessionCreateDto sessionCreateDto) {
        try {
            log.info("SessionClient: getSession()");
            var uriBuilder = UriComponentsBuilder
                    .fromUriString(sessionManagerProperties.getSessionProviderUrl())
                    .queryParam("login", sessionCreateDto.login());
            return restTemplate.getForObject(uriBuilder.toUriString(), SessionDto.class);
        } catch (RestClientException ex) {
            log.error("SessionClient: exception - " + ex.getMessage());
            throw new SessionServiceException(ex.getMessage());
        }
    }
}