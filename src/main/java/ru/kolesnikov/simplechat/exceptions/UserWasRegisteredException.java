package ru.kolesnikov.simplechat.exceptions;

import com.devskiller.friendly_id.FriendlyId;

import java.text.MessageFormat;
import java.util.UUID;

public class UserWasRegisteredException extends RuntimeException {

    public UserWasRegisteredException(String login) {
        super(MessageFormat.format("{0} {1} was already registered",
                FriendlyId.toFriendlyId(UUID.randomUUID()),
                login));
    }
}
