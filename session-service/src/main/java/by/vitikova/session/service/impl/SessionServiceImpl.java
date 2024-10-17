package by.vitikova.session.service.impl;

import by.vitikova.session.converter.SessionConverter;
import by.vitikova.session.exception.OperationException;
import by.vitikova.session.model.dto.SessionDto;
import by.vitikova.session.model.entity.Session;
import by.vitikova.session.repository.SessionRepository;
import by.vitikova.session.service.SessionService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Реализация сервиса для управления сессиями пользователей.
 */
@Service
@AllArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionConverter sessionConverter;
    private final SessionRepository sessionRepository;
    private static final Logger logger = LoggerFactory.getLogger(SessionServiceImpl.class);

    /**
     * Получает сессию пользователя по его логину.
     * Если сессия не найдена, создает новую сессию.
     *
     * @param login логин пользователя
     * @return DTO объект сессии
     */
    @Override
    public SessionDto getSession(String login) {
        logger.info("SessionServiceImpl: get session by login - " + login);
        return sessionConverter.convert(sessionRepository.findByLogin(login).orElse(sessionConverter.convert(createSession(login))));
    }

    /**
     * Создает новую сессию пользователя.
     * Если сессия с таким логином уже существует, возвращает ее.
     *
     * @param login логин пользователя
     * @return DTO объект сессии
     * @throws OperationException если возникает ошибка при создании сессии
     */
    private SessionDto createSession(String login) {
        try {
            logger.info("SessionServiceImpl: create session with login - " + login);
            if (sessionRepository.existsByLogin(login)) {
                logger.info("SessionServiceImpl: get session by login - " + login);
                return sessionConverter.convert(sessionRepository.findByLogin(login).get());
            }
            var session = new Session(login);
            session.setDateCreate(LocalDateTime.now());
            return sessionConverter.convert(sessionRepository.save(session));
        } catch (Exception ex) {
            logger.error("SessionServiceImpl: exception - " + ex);
            throw new OperationException("Create session exception: " + ex);
        }
    }
}