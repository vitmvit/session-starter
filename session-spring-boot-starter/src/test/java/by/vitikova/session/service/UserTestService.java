package by.vitikova.session.service;

import by.vitikova.session.SessionDto;
import by.vitikova.session.annotation.SessionManagement;
import by.vitikova.session.model.UserOpenTimeDto;
import org.springframework.stereotype.Component;

@Component
public class UserTestService {

    @SessionManagement(blackList = {"admin"}, includeDefaultBlackListSource = false)
    public SessionDto getSession(UserOpenTimeDto userCreateDto, SessionDto sessionDto) {
        return sessionDto;
    }
}