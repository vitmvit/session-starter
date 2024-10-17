package by.vitikova.session.service;

import by.vitikova.session.model.dto.SessionDto;

public interface SessionService {

    SessionDto getSession(String login);
}