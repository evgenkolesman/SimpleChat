package ru.kolesnikov.simplechat.controller.dto;

import java.time.Instant;


public record MessageDTOResponse(String id,
                                 String login,
                                 String messageBody,
                                 Instant dateMessage) {

}
