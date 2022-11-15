package ru.kolesnikov.simplechat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kolesnikov.simplechat.exceptions.NotAuthorizedException;
import ru.kolesnikov.simplechat.service.AuthService;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class AuthUserController {

    private final AuthService authService;

    @GetMapping(value = "/api/v1/user/{login}/activeUsers")
    public List<String> getAllActiveUsers(@PathVariable String login) {
        if (!authService.checkAccess(login)) {
            throw new NotAuthorizedException();
        }
        return authService.getAllActiveLogins();
    }


}
