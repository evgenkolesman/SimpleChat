package ru.kolesnikov.simplechat.exceptions;

import com.devskiller.friendly_id.FriendlyId;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;
import java.util.UUID;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String login) {
        super(MessageFormat.format("{0} {1} not found", FriendlyId.toFriendlyId(UUID.randomUUID()), login));
    }
}
