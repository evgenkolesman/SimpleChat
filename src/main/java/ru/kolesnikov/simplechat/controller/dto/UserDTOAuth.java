package ru.kolesnikov.simplechat.controller.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public record UserDTOAuth(
        @NotNull(message = "Invalid data: login field should be filled")
        @Size(min = 2, message = "Invalid data: login must be minimum 2 characters long")
        String login,
        @NotNull(message = "Invalid data: password field should be filled")
        @Size(min = 2, message = "Invalid data: password must be minimum 8 characters long")
        String password) {
}
