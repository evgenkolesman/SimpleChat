package ru.kolesnikov.simplechat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.kolesnikov.simplechat.controller.dto.UserDTOAuth;
import ru.kolesnikov.simplechat.controller.dto.UserDTOResponse;
import ru.kolesnikov.simplechat.model.User;
import ru.kolesnikov.simplechat.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class AuthUserController {

    private final UserService userService;

    @PostMapping(value = "/api/v1/auth")
    public boolean checkUserAuthorization(@RequestBody @Valid UserDTOAuth user) {
        return userService.checkAuthorization(user);
    }
}
