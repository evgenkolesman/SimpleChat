package ru.kolesnikov.simplechat.controller.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.Instant;

@Getter
@ToString
public final class MessageDTORequest {

    private final String messageBody;

    private final Instant dateMessage;

    public MessageDTORequest(String login, String messageBody) {
        this.messageBody = messageBody;
        this.dateMessage = Instant.now();
    }

}
