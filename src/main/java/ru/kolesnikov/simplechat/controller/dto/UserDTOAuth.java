package ru.kolesnikov.simplechat.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RequiredArgsConstructor
@Getter
public class UserDTOAuth {
    @NotNull(message = "Invalid data: login field should be filled")
    @Size(min = 2, message = "Invalid data: login must be minimum 2 characters long")
    private final String login;
    @NotNull(message = "Invalid data: password field should be filled")
    @Size(min = 2, message = "Invalid data: password must be minimum 8 characters long")
    private final String password;
}
