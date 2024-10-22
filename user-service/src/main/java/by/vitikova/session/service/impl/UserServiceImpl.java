package by.vitikova.session.service.impl;

import by.vitikova.session.SessionDto;
import by.vitikova.session.converter.UserConverter;
import by.vitikova.session.exception.OperationException;
import by.vitikova.session.model.dto.UserDto;
import by.vitikova.session.model.dto.UserOpenTimeDto;
import by.vitikova.session.repository.UserRepository;
import by.vitikova.session.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Реализация сервиса для работы с пользователями.
 * <p>
 * Этот класс предоставляет методы для поиска и создания пользователей.
 * Он использует {@link UserRepository} для доступа к данным пользователей
 * и {@link UserConverter} для преобразования объектов DTO.
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userMapper;

    /**
     * Находит пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return объект {@link UserDto}, соответствующий найденному пользователю
     * @throws OperationException если пользователь не найден
     */
    @Override
    public UserDto findById(Long id) {
        log.info("UserServiceImpl: findById() - id = " + id);
        return userMapper.convert(userRepository.findById(id).orElseThrow(() -> new OperationException("User not found!")));
    }

    /**
     * Создает нового пользователя на основе переданных данных.
     *
     * @param userCreateDto объект DTO, содержащий данные для создания пользователя
     * @param sessionDto    объект сессии, связанный с создаваемым пользователем
     * @return объект {@link UserDto}, представляющий созданного пользователя
     * @throws OperationException если возникла ошибка при создании пользователя
     */
    @Override
    @Transactional
    public UserDto create(UserOpenTimeDto userCreateDto, SessionDto sessionDto) {
        try {
            log.info("UserServiceImpl: create() - login = " + userCreateDto.login());
            var user = userMapper.convertToCreateDto(userCreateDto);
            user.setSessionOpen(sessionDto.dateCreate());
            return userMapper.convert(userRepository.save(userMapper.convert(user)));
        } catch (Exception ex) {
            log.error("UserServiceImpl: exception - " + ex.getMessage());
            throw new OperationException("User create exception: " + ex.getMessage());
        }
    }
}