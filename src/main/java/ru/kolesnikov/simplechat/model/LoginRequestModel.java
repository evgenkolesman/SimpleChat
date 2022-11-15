package ru.kolesnikov.simplechat.model;

import lombok.Data;

@Data
public class LoginRequestModel {
    private String login;
    private String password;
}
