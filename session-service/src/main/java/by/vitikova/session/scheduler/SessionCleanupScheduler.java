package by.vitikova.session.scheduler;

import by.vitikova.session.exception.ScheduleException;
import by.vitikova.session.repository.SessionRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Планировщик для очистки старых сессий из репозитория.
 * Выполняет периодическую очистку сессий, созданных более 24 часов назад.
 */
@Component
@AllArgsConstructor
public class SessionCleanupScheduler {

    private final SessionRepository sessionRepository;
    private static final Logger logger = LoggerFactory.getLogger(SessionCleanupScheduler.class);

    /**
     * Метод, выполняющийся по расписанию для очистки старых сессий.
     * Запускается каждую минуту согласно cron-выражению "0 * * * * ?".
     */
    @Scheduled(cron = "0 * * * * ?")
    public void cleanUpOldSessions() {
        try {
            LocalDateTime cutoffDateTime = LocalDateTime.now().minus(1, ChronoUnit.DAYS);
            sessionRepository.deleteAll(sessionRepository.findByDateCreateBefore(cutoffDateTime));
            logger.info("SessionCleanupScheduler: cleared old sessions older than 24 hours");
        } catch (Exception ex) {
            logger.error("SessionCleanupScheduler: exception - " + ex.getMessage());
            throw new ScheduleException("Schedule exception: " + ex.getMessage());
        }
    }
}