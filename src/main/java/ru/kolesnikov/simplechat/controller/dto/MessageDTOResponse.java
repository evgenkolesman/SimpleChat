package ru.kolesnikov.simplechat.controller.dto;

import lombok.*;
import ru.kolesnikov.simplechat.model.Message;

import java.time.Instant;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class MessageDTOResponse {

    private final String id;

    private final String login;

    private final String messageBody;

    private final Instant dateMessage;

}
