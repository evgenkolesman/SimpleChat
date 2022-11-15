package ru.kolesnikov.simplechat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kolesnikov.simplechat.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findUserByLoginAndPassword(String login, String password);

    @Modifying
    @Override
    @Query(value = "delete from users where login = ?", nativeQuery = true)
    void delete(User entity);

    @Modifying
    @Query(value = "delete from users ", nativeQuery = true)
    void deleteAll();

    Optional<User> findUserByLogin(String login);
}
