package ru.kolesnikov.simplechat.exceptions;

import java.text.MessageFormat;

public class BadLoginMessageException extends RuntimeException {

    public BadLoginMessageException(String loginFromMessage,
                                    String login) {
        super(MessageFormat.format(
                        "Login from message {0} not equals to login from url {1}",
                        loginFromMessage,
                        login));

    }
}
