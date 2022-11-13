package ru.kolesnikov.simplechat.controller.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

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
