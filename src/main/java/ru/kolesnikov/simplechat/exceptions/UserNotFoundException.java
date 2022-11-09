package ru.kolesnikov.simplechat.exceptions;

import com.devskiller.friendly_id.FriendlyId;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super(FriendlyId.toFriendlyId(UUID.randomUUID()) + " user not found");
    }
}
