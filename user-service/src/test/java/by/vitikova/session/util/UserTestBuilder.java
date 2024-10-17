package by.vitikova.session.util;

import by.vitikova.session.SessionDto;
import by.vitikova.session.model.dto.UserCreateDto;
import by.vitikova.session.model.dto.UserDto;
import by.vitikova.session.model.dto.UserOpenTimeDto;
import by.vitikova.session.model.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(setterPrefix = "with")
public class UserTestBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String login = "user_1";

    @Builder.Default
    private String password = "user_1";

    @Builder.Default
    private LocalDateTime sessionOpen = LocalDateTime.of(2024, 1, 22, 5, 34, 45, 23);

    public User buildUser() {
        return new User(id, login, password, sessionOpen);
    }

    public UserDto buildUserDto() {
        return new UserDto(id, login, password, sessionOpen);
    }

    public UserCreateDto buildUserCreateDto() {
        return new UserCreateDto(login, password, sessionOpen);
    }

    public UserOpenTimeDto buildUserOpenTimeDto() {
        return new UserOpenTimeDto(login, password);
    }

    public SessionDto buildSessionDto() {
        return new SessionDto("67111d1aa30e7e7092ef6b19", login, sessionOpen);
    }
}