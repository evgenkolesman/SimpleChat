package ru.kolesnikov.simplechat.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolesnikov.simplechat.model.AccessModel;

import java.util.List;

@Repository
public interface AuthRepository extends JpaRepository<AccessModel, Long> {

    boolean existsAccessModelByLogin(String login);

    @NonNull List<AccessModel> findAll();

    @Transactional
    void deleteByLogin(String login);

    void deleteAll();
}
