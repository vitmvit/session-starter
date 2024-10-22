package by.vitikova.session.service;

import by.vitikova.session.SessionDto;

public interface SessionService {

    SessionDto getSession(String login);
}