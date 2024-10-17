package by.vitikova.session.model.dto;

import by.vitikova.session.SessionInfo;

public record UserOpenTimeDto(String login,
                              String password
) implements SessionInfo {
}