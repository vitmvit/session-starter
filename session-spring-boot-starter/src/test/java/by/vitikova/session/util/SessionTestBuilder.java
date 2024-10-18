package by.vitikova.session.util;

import by.vitikova.session.SessionDto;
import by.vitikova.session.model.UserOpenTimeDto;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(setterPrefix = "with")
public class SessionTestBuilder {

    @Builder.Default
    private String id = "67111d1aa30e7e7092ef6b19";

    @Builder.Default
    private String login = "user_1";

    @Builder.Default
    private LocalDateTime dateCreate = LocalDateTime.of(2024, 1, 22, 5, 34, 45, 23);

    public SessionDto buildSessionDto() {
        return new SessionDto(id, login, dateCreate);
    }

    public UserOpenTimeDto buildUserOpenTimeDto() {
        return new UserOpenTimeDto(login, login);
    }
}