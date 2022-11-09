package ru.kolesnikov.simplechat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kolesnikov.simplechat.model.Message;
import ru.kolesnikov.simplechat.model.User;
import ru.kolesnikov.simplechat.service.MessageService;
import ru.kolesnikov.simplechat.service.UserService;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ApiController {

    private final UserService userService;
    private final MessageService messageService;

    @GetMapping("/api/v1/getUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    @GetMapping("/api/v1/getMessages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }
}
