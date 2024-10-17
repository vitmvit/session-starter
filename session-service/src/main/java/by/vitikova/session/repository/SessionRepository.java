package by.vitikova.session.repository;

import by.vitikova.session.model.entity.Session;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SessionRepository extends MongoRepository<Session, String> {

    Optional<Session> findByLogin(String login);

    Boolean existsByLogin(String login);

    List<Session> findByDateCreateBefore(LocalDateTime cutoffDateTime);
}