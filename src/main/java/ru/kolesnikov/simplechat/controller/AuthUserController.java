package ru.kolesnikov.simplechat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kolesnikov.simplechat.controller.dto.UserDTOAuth;
import ru.kolesnikov.simplechat.service.UserService;

import javax.validation.Valid;

import static ru.kolesnikov.simplechat.kafka.KafkaTopicConfig.KAFKA_TOPIC;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class AuthUserController {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final UserService userService;

    @PostMapping(value = "/api/v1/auth")
    public boolean checkUserAuthorization(@RequestBody @Valid UserDTOAuth user) {
        String data;
        boolean checkAuthorization = userService.checkAuthorization(user);
        if(checkAuthorization){
            data = "AUTHORIZATION_TOKEN";
        } else {
            data = "NOT AUTHORIZED";
        }
        kafkaTemplate.send(KAFKA_TOPIC,
                data);
        return checkAuthorization;
    }
}
