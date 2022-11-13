package ru.kolesnikov.simplechat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import ru.kolesnikov.simplechat.controller.dto.UserDTOAuth;
import ru.kolesnikov.simplechat.exceptions.NotAuthorizedException;
import ru.kolesnikov.simplechat.service.AuthService;
import ru.kolesnikov.simplechat.service.UserService;

import javax.validation.Valid;
import java.util.List;

import static ru.kolesnikov.simplechat.kafka.KafkaTopicConfig.KAFKA_TOPIC;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class AuthUserController {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final UserService userService;
    private final AuthService authService;

    @PostMapping(value = "/api/v1/auth")
    public boolean checkUserAuthorization(@RequestBody @Valid UserDTOAuth user) {

        boolean checkAuthorization = userService.checkAuthorization(user);

        if (checkAuthorization) {
            authService.authorize(user.getLogin());
            kafkaTemplate.send(KAFKA_TOPIC,
                    "AUTHORIZATION_TOKEN");
        } else {
            log.info("NOT AUTHORIZED");
        }

        return checkAuthorization;
    }

    @DeleteMapping(value = "/api/v1/user/{login}/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@PathVariable String login) {
        if (!authService.checkAccess(login)) {
            throw new NotAuthorizedException();
        }
        ;
        authService.logout(login);
    }

    @GetMapping(value = "/api/v1/user/{login}/activeUsers")
    public List<String> getAllActiveUsers(@PathVariable String login) {
        if (!authService.checkAccess(login)) {
            throw new NotAuthorizedException();
        }
        ;
        return authService.getAllActiveLogins();
    }


}
