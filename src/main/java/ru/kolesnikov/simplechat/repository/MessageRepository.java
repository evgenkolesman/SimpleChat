package ru.kolesnikov.simplechat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kolesnikov.simplechat.model.Message;
import ru.kolesnikov.simplechat.model.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {

    List<Message> findAllByUser(User user);

    List<Message> findAllByDateOfMessageAfterAndDateOfMessageBefore(Instant startDate, Instant EndDate);

    Optional<Message> findMessageById(String id);

}
