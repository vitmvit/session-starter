package by.vitikova.session;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
public final class SessionCreateDto implements SessionInfo {
    private final String login;

    @Override
    public String login() {
        return login;
    }
}