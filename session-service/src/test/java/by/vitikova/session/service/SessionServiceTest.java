package by.vitikova.session.service;

import by.vitikova.session.converter.SessionConverter;
import by.vitikova.session.model.entity.Session;
import by.vitikova.session.repository.SessionRepository;
import by.vitikova.session.service.impl.SessionServiceImpl;
import by.vitikova.session.util.SessionTestBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository userRepository;

    @Mock
    private SessionConverter userConverter;

    @InjectMocks
    private SessionServiceImpl userService;

    @Test
    void getSessionShouldReturnExpectedSessionWhenFound() {
        var sessionDto = SessionTestBuilder.builder().build().buildUSessionDto();
        var expected = SessionTestBuilder.builder().build().buildSession();
        var login = expected.getLogin();

        when(userRepository.existsByLogin(login)).thenReturn(true);
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(expected));
        when(userConverter.convert(expected)).thenReturn(sessionDto);

        var actual = userService.getSession(login);

        assertThat(actual)
                .isNotNull()
                .hasFieldOrPropertyWithValue(Session.Fields.id, expected.getId())
                .hasFieldOrPropertyWithValue(Session.Fields.login, expected.getLogin())
                .hasFieldOrPropertyWithValue(Session.Fields.dateCreate, expected.getDateCreate());
    }
}