package by.vitikova.session.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UserCreateDto {

    private String login;
    private String password;
    private LocalDateTime sessionOpen;
}