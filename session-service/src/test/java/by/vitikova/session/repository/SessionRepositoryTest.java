package by.vitikova.session.repository;

import by.vitikova.session.config.MongoDbContainerInitializer;
import by.vitikova.session.model.entity.Session;
import by.vitikova.session.util.SessionTestBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SessionRepositoryTest extends MongoDbContainerInitializer {

    @Autowired
    private SessionRepository sessionRepository;

    @Test
    void findByLoginShouldReturnExpectedSession() {
        var expected = sessionRepository.save(SessionTestBuilder.builder().withLogin("user_0").build().buildSession());
        var login = expected.getLogin();

        var actual = sessionRepository.findByLogin(login);

        assertThat(actual)
                .get()
                .isNotNull()
                .hasFieldOrPropertyWithValue(Session.Fields.id, expected.getId())
                .hasFieldOrPropertyWithValue(Session.Fields.login, expected.getLogin());
    }

    @Test
    void findByLoginShouldReturnOptionalEmpty() {
        var login = "user_3";

        var actual = sessionRepository.findByLogin(login);

        assertEquals(Optional.empty(), actual);
    }

    @Test
    void existsByLoginShouldReturnTrue() {
        var expected = sessionRepository.save(SessionTestBuilder.builder().build().buildSession());
        var login = expected.getLogin();

        assertEquals(true, sessionRepository.existsByLogin(login));
    }

    @Test
    void existsByLoginShouldReturnFalse() {
        var login = "user_4";

        assertEquals(false, sessionRepository.existsByLogin(login));
    }

    @Test
    void findByDateCreateBeforeShouldReturnExpectedSizeList() {
        sessionRepository.save(SessionTestBuilder.builder().withLogin("user_5").build().buildSession());

        var actual = sessionRepository.findByDateCreateBefore(LocalDateTime.now());

        Assertions.assertTrue(actual.size() > 0);
    }
}