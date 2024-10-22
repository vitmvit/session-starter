package by.vitikova.session.controller;

import by.vitikova.session.SessionCreateDto;
import by.vitikova.session.SessionDto;
import by.vitikova.session.annotation.SessionManagement;
import by.vitikova.session.model.dto.UserDto;
import by.vitikova.session.model.dto.UserOpenTimeDto;
import by.vitikova.session.provider.FileBlackListProvider;
import by.vitikova.session.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @SessionManagement(blackList = {"admin", "user"}, blackListProviders = {FileBlackListProvider.class})
    public ResponseEntity<UserDto> findById(@RequestParam(value = "login", required = false) SessionCreateDto createDto,
                                            @PathVariable Long id,
                                            SessionDto sessionDto) {
        log.info("UserController: findById() - session = {}, login = {}", sessionDto.toString(), createDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SessionManagement(blackList = {"editor", "user"}, includeDefaultBlackListSource = false)
    public ResponseEntity<UserDto> create(@RequestBody UserOpenTimeDto userCreateDto, SessionDto sessionDto) {
        log.info("UserController: create() - session = {}, user = {}", sessionDto.toString(), userCreateDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.create(userCreateDto, sessionDto));
    }
}