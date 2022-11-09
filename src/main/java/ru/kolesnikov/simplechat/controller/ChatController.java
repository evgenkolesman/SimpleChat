package ru.kolesnikov.simplechat.controller;


import com.devskiller.friendly_id.FriendlyId;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kolesnikov.simplechat.controller.dto.MessageDTO;
import ru.kolesnikov.simplechat.controller.dto.UserDTO;
import ru.kolesnikov.simplechat.model.Message;
import ru.kolesnikov.simplechat.model.User;
import ru.kolesnikov.simplechat.service.MessageService;
import ru.kolesnikov.simplechat.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;
    private final UserService userService;


    @MessageMapping("/addMessage")
    @SendTo("/public")
    public Message addMessage(@Payload MessageDTO messageDTO) {
        return messageService.addMesage(
                new Message(FriendlyId.toFriendlyId(UUID.randomUUID()),
                        messageDTO.message(),
                        messageDTO.dateOfMessage(),
                        userService.findUserByLogin(messageDTO.login())));
    }

    @MessageMapping("/addUser")
    @SendTo("/public")
    public User addUser(UserDTO user) {
        return userService.addUser(new User(user.login(), user.password()));
    }
}
