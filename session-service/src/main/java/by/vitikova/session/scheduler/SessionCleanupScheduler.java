package by.vitikova.session.scheduler;

import by.vitikova.session.exception.ScheduleException;
import by.vitikova.session.repository.SessionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Планировщик для очистки старых сессий из репозитория.
 * Выполняет периодическую очистку сессий, созданных более 24 часов назад.
 */
@Slf4j
@Component
@AllArgsConstructor
public class SessionCleanupScheduler {

    private final SessionRepository sessionRepository;

    /**
     * Метод, выполняющийся по расписанию для очистки старых сессий.
     * Запускается каждую минуту согласно cron-выражению "0 * * * * ?".
     */
    @Scheduled(cron = "0 * * * * ?")
    public void cleanUpOldSessions() {
        try {
            var cutoffDateTime = LocalDateTime.now().minus(1, ChronoUnit.DAYS);
            sessionRepository.deleteAll(sessionRepository.findByDateCreateBefore(cutoffDateTime));
            log.info("SessionCleanupScheduler: cleared old sessions older than 24 hours");
        } catch (Exception ex) {
            log.error("SessionCleanupScheduler: exception - " + ex.getMessage());
            throw new ScheduleException("Schedule exception: " + ex.getMessage());
        }
    }
}