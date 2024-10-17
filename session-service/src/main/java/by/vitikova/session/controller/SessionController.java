package by.vitikova.session.controller;

import by.vitikova.session.model.dto.SessionDto;
import by.vitikova.session.service.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/sessions")
public class SessionController {

    private final SessionService sessionService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SessionDto getSession(@RequestParam("login") String login) {
        return sessionService.getSession(login);
    }
}