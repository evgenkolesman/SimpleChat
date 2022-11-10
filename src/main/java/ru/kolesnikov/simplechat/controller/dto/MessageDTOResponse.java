package ru.kolesnikov.simplechat.controller.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.kolesnikov.simplechat.model.Message;

import java.time.Instant;

@Getter
@ToString
@RequiredArgsConstructor
public final class MessageDTOResponse {

    private final String id;

    private final String login;

    private final String messageBody;

    private final Instant dateMessage;

}
