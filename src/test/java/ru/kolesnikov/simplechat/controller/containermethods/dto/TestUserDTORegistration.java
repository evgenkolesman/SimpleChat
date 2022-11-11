package ru.kolesnikov.simplechat.controller.containermethods.dto;

import lombok.Data;

@Data
public class TestUserDTORegistration {

    private final String login;
    private final String name;
    private final String surname;
    private final String password;
    private final String photoPath;
}
