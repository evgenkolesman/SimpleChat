package ru.kolesnikov.simplechat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolesnikov.simplechat.model.AccessModel;

import java.util.List;

@Repository
public interface AuthRepository extends JpaRepository<AccessModel, Long> {

    boolean existsAccessModelByLogin(String userByLogin);

    List<AccessModel> findAll();

    @Modifying
    @Transactional
    @Query(value = "delete from access where access.login = :login1", nativeQuery = true)
    void deleteByLogin(String login1);

    @Modifying
    @Query(value = "delete from access ", nativeQuery = true)
    void deleteAll();
}
