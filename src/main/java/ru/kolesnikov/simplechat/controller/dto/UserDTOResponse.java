package ru.kolesnikov.simplechat.controller.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class UserDTOResponse {

    private final String login;

    private final String name;

    private final String photoPath;
}
