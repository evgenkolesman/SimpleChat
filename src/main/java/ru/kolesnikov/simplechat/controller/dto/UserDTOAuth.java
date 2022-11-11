package ru.kolesnikov.simplechat.controller.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserDTOAuth {
    @NonNull
    private final String login;
    @NonNull
    private final String password;
}
