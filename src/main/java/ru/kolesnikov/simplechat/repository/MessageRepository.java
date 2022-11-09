package ru.kolesnikov.simplechat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kolesnikov.simplechat.model.Message;
import ru.kolesnikov.simplechat.model.User;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
}
