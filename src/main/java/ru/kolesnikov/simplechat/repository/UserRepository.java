package ru.kolesnikov.simplechat.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kolesnikov.simplechat.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    void delete(@NonNull User entity);

    void deleteAll();

    Optional<User> findUserByLogin(String login);
}
