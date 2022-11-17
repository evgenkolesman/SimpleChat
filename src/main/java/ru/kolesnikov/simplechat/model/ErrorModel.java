package ru.kolesnikov.simplechat.model;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
//@JsonDeserialize(as = ErrorModel.class)
public class ErrorModel {
    private final String id;

    private final String message;

    private final HttpStatus status;
}
