package ru.kolesnikov.simplechat.controller.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RequiredArgsConstructor
@Getter
public class UserDTORegistration {
    @NotNull(message = "Invalid data: login field should be filled")
    @Size(min = 2, message = "Invalid data: login must be minimum 2 characters long")
    private final String login;
    @NotNull(message = "Invalid data: name field should be filled")
    @Size(min = 2, message = "Invalid data: name must be minimum 2 characters long")
    private final String name;
    @NotNull(message = "Invalid data: surname field should be filled")
    @Size(min = 2, message = "Invalid data: surname must be minimum 2 characters long")
    private final String surname;
    @NotNull(message = "Invalid data: password field should be filled")
    @Size(min = 2, message = "Invalid data: password must be minimum 8 characters long")
    private final String password;

    private final String photoPath;
}
