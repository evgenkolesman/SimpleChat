package ru.kolesnikov.simplechat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kolesnikov.simplechat.exceptions.MessageNotFoundException;
import ru.kolesnikov.simplechat.model.Message;
import ru.kolesnikov.simplechat.repository.MessageRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public List<Message> getAllMessages(Instant dateStart, Instant dateEnd) {
        if(Optional.ofNullable(dateStart).isEmpty() && Optional.ofNullable(dateEnd).isEmpty() ){
            return getAllMessages();
        } else if (Optional.ofNullable(dateStart).isEmpty()) {
            dateStart = dateEnd;
        } else if (Optional.ofNullable(dateEnd).isEmpty()) {
            dateEnd = Instant.now();
        }
        return messageRepository.findAllByDateOfMessageAfterAndDateOfMessageBefore(dateStart, dateEnd);
    }

    public List<Message> getAllMessages(String login) {
        return messageRepository.findAllByUser(userService.findUserByLogin(login));
    }

    public Message addMessage(Message message) {
        return messageRepository.save(message);
    }

    public Message updateMessage(Message message) {
       if (checkGetMessageById(message.getId())) {
       throw new MessageNotFoundException(message.getId());
       }
        return messageRepository.save(message);
    }

    public Message getMessageById(String id) {
        return messageRepository
                .findMessageById(id)
                .orElseThrow(() -> new MessageNotFoundException(id));
    }

    public boolean checkGetMessageById(String id) {
        return messageRepository
                .findMessageById(id)
                .isPresent();
    }

    public void deleteMessage(String id) {
        if (checkGetMessageById(id)) {
            throw new MessageNotFoundException(id);
        }
        messageRepository.deleteById(id);
    }
}
