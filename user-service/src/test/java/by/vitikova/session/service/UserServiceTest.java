package by.vitikova.session.service;

import by.vitikova.session.converter.UserConverter;
import by.vitikova.session.exception.OperationException;
import by.vitikova.session.model.entity.User;
import by.vitikova.session.repository.UserRepository;
import by.vitikova.session.service.impl.UserServiceImpl;
import by.vitikova.session.util.UserTestBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findByIdShouldReturnExpectedUserWhenFound() {
        var expected = UserTestBuilder.builder().build().buildUser();
        var userDto = UserTestBuilder.builder().build().buildUserDto();
        var id = expected.getId();

        when(userRepository.findById(id)).thenReturn(Optional.of(expected));
        when(userConverter.convert(expected)).thenReturn(userDto);

        var actual = userService.findById(id);

        assertThat(actual)
                .isNotNull()
                .hasFieldOrPropertyWithValue(User.Fields.id, expected.getId())
                .hasFieldOrPropertyWithValue(User.Fields.login, expected.getLogin())
                .hasFieldOrPropertyWithValue(User.Fields.password, expected.getPassword())
                .hasFieldOrPropertyWithValue(User.Fields.sessionOpen, expected.getSessionOpen());
    }

    @Test
    void findByIdShouldThrowOperationExceptionWhenUserNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        var exception = assertThrows(OperationException.class, () -> userService.findById(2L));
        assertEquals(exception.getClass(), OperationException.class);
    }

    @Test
    void createShouldReturnUserDtoWhenSuccessful() {
        var userOpenTimeDto = UserTestBuilder.builder().build().buildUserOpenTimeDto();
        var userDto = UserTestBuilder.builder().build().buildUserDto();
        var expectedUserCreateDto = UserTestBuilder.builder().build().buildUserCreateDto();
        var expectedUser = UserTestBuilder.builder().build().buildUser();
        var sessionDto = UserTestBuilder.builder().build().buildSessionDto();

        when(userConverter.convertToCreateDto(userOpenTimeDto)).thenReturn(expectedUserCreateDto);
        when(userConverter.convert(expectedUserCreateDto)).thenReturn(expectedUser);
        when(userRepository.save(expectedUser)).thenReturn(expectedUser);
        when(userConverter.convert(expectedUser)).thenReturn(userDto);

        var actual = userService.create(userOpenTimeDto, sessionDto);

        assertThat(actual)
                .isNotNull()
                .hasFieldOrPropertyWithValue(User.Fields.id, expectedUser.getId())
                .hasFieldOrPropertyWithValue(User.Fields.login, expectedUser.getLogin())
                .hasFieldOrPropertyWithValue(User.Fields.password, expectedUser.getPassword())
                .hasFieldOrPropertyWithValue(User.Fields.sessionOpen, expectedUser.getSessionOpen());
    }
}