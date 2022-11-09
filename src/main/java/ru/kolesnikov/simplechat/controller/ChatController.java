package ru.kolesnikov.simplechat.controller;


import com.devskiller.friendly_id.FriendlyId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kolesnikov.simplechat.controller.dto.MessageDTO;
import ru.kolesnikov.simplechat.controller.dto.UserDTO;
import ru.kolesnikov.simplechat.model.Message;
import ru.kolesnikov.simplechat.model.User;
import ru.kolesnikov.simplechat.service.MessageService;
import ru.kolesnikov.simplechat.service.UserService;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final MessageService messageService;
    private final UserService userService;


    @MessageMapping("/addMessage")
    @SendTo("/topic/public")
    public Message addMessage(@Payload MessageDTO messageDTO) {
        log.info(messageDTO.toString());
        return messageService.addMessage(
                new Message(FriendlyId.toFriendlyId(UUID.randomUUID()),
                        messageDTO.getMessageBody(),
                        messageDTO.getDateMessage(),
                        userService.findUserByLogin(messageDTO.getLogin())));
    }

    @MessageMapping("/addUser")
    @SendTo("/topic/public")
    public User addUser(@Payload MessageDTO messageDTO) {
        log.info("new user" + messageDTO.getLogin());

        return userService.addUser(new User(messageDTO.getLogin()));
    }


    @MessageMapping("/getPermissions")
    @SendTo("/topic/public")
    public User getPermissions(@Payload MessageDTO messageDTO) {
        log.info("new user" + messageDTO.getLogin());

        return userService.getUser(new User(messageDTO.getLogin()));
    }
}
