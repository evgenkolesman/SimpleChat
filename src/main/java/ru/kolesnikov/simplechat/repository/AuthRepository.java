package ru.kolesnikov.simplechat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolesnikov.simplechat.model.AccessModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<AccessModel, Long> {

    @Transactional
    @Query(value = "select access.id, access.login from access where access.login = :login limit 1", nativeQuery = true)
    Optional<AccessModel> existsAccessModelByLogin(String login);

    List<AccessModel> findAll();

    @Modifying
    @Transactional
    @Query(value = "delete from access where access.login = :login", nativeQuery = true)
    void deleteByLogin(String login);

    @Modifying
    @Query(value = "delete from access ", nativeQuery = true)
    void deleteAll();
}
