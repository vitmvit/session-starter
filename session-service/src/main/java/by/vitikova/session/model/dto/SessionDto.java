package by.vitikova.session.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class SessionDto {

    private String id;
    private String login;
    private LocalDateTime dateCreate;
}