package by.vitikova.session.service;

import by.vitikova.session.SessionDto;
import by.vitikova.session.model.dto.UserDto;
import by.vitikova.session.model.dto.UserOpenTimeDto;

public interface UserService {

    UserDto findById(Long id);

    UserDto create(UserOpenTimeDto userCreateDto, SessionDto sessionDto);
}