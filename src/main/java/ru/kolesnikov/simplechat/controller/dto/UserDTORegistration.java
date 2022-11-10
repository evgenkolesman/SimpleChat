package ru.kolesnikov.simplechat.controller.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserDTORegistration {
    @NonNull
    private final String login;
    @NonNull
    private final String name;
    @NonNull
    private final String surname;
    @NonNull
    private final String password;

    private final String photoPath;
}
