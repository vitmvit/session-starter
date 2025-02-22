package by.vitikova.session.model;

import by.vitikova.session.SessionInfo;

public record UserOpenTimeDto(String login,
                              String password
) implements SessionInfo {
}