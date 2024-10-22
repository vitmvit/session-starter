package by.vitikova.session;

import java.time.LocalDateTime;

public record SessionDto(String id,
                         String login,
                         LocalDateTime dateCreate) {
}