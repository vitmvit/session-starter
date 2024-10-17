package by.vitikova.session.model.dto;

import java.time.LocalDateTime;

public record UserDto(Long id,
                      String login,
                      String password,
                      LocalDateTime sessionOpen) {
}
