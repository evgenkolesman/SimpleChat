package ru.kolesnikov.simplechat.controller;

import com.devskiller.friendly_id.FriendlyId;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.kolesnikov.simplechat.controller.dto.MessageDTORequest;
import ru.kolesnikov.simplechat.controller.dto.MessageDTOResponse;
import ru.kolesnikov.simplechat.model.Message;
import ru.kolesnikov.simplechat.service.MessageService;
import ru.kolesnikov.simplechat.service.UserService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    @PostMapping("/api/v1/user/{login}/messages")
    public MessageDTOResponse addMessage(@PathVariable String login,
                                         @RequestBody MessageDTORequest messageDTORequest) {

        Message message = messageService.addMessage(
                new Message(
                        FriendlyId.createFriendlyId(),
                        messageDTORequest.messageBody(),
                        Instant.now().truncatedTo(ChronoUnit.MICROS),
                        userService.findUserByLogin(login)
                ));

        return new MessageDTOResponse(
                message.getId(),
                message.getUser().getLogin(),
                message.getMessageBody(),
                message.getDateOfMessage()
        );
    }

    @GetMapping("/api/v1/user/{login}/messages")
    public List<MessageDTOResponse> getAllMessagesWithLogin(@PathVariable String login) {

        return messageService.getAllMessages(login).stream()
                .map(message ->
                        new MessageDTOResponse(
                                message.getId(),
                                message.getUser().getLogin(),
                                message.getMessageBody(),
                                message.getDateOfMessage()
                        )).collect(Collectors.toList());
    }

    @PutMapping("/api/v1/user/{login}/messages/{id}")
    public MessageDTOResponse updateMessage(@PathVariable String login,
                                            @PathVariable String id,
                                            @RequestBody MessageDTORequest messageDTORequest) {

        Message message = messageService.updateMessage(
                new Message(id,
                        messageDTORequest.messageBody(),
                        Instant.now().truncatedTo(ChronoUnit.MICROS),
                        userService.findUserByLogin(login)));

        return new MessageDTOResponse(
                message.getId(),
                message.getUser().getLogin(),
                message.getMessageBody(),
                message.getDateOfMessage()
        );
    }

    @GetMapping("/api/v1/user/{login}/messages/{id}")
    public MessageDTOResponse getMessageById(@PathVariable String login,
                                             @PathVariable String id) {

        Message message = messageService.getMessageById(login, id);

        return new MessageDTOResponse(
                message.getId(),
                message.getUser().getLogin(),
                message.getMessageBody(),
                message.getDateOfMessage()
        );
    }

    @DeleteMapping("/api/v1/user/{login}/messages/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(@PathVariable String login,
                              @PathVariable String id) {
        messageService.deleteMessage(id);
    }


    @GetMapping("/api/v1/allmessages")
    public List<MessageDTOResponse> getAllMessages(@RequestParam(name = "dateStart")
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                   @Nullable Instant dateStart,
                                                   @RequestParam(name = "dateEnd")
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                   @Nullable Instant dateEnd) {
        return messageService.getAllMessages(dateStart, dateEnd).stream()
                .map(message ->
                        new MessageDTOResponse(
                                message.getId(),
                                message.getUser().getLogin(),
                                message.getMessageBody(),
                                message.getDateOfMessage()
                        )).collect(Collectors.toList());
    }


}
