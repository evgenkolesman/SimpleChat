package ru.kolesnikov.simplechat.controller.dto;

import lombok.EqualsAndHashCode;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
public record MessageDTO(String login, String message, Instant dateOfMessage) {

}
