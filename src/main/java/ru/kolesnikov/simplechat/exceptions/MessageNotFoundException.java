package ru.kolesnikov.simplechat.exceptions;

import com.devskiller.friendly_id.FriendlyId;

import java.text.MessageFormat;
import java.util.UUID;

public class MessageNotFoundException extends RuntimeException {

    public MessageNotFoundException(String id) {
        super(MessageFormat.format("{0} Problems with message ID: {1} not found", FriendlyId.toFriendlyId(UUID.randomUUID()), id));
    }
}
