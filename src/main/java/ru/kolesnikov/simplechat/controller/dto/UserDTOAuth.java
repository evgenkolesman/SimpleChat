package ru.kolesnikov.simplechat.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserDTOAuth {

    private final String login;
    private final String password;
}
