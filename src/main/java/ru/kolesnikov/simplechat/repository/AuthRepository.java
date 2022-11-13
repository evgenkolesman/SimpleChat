package ru.kolesnikov.simplechat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kolesnikov.simplechat.model.AccessModel;
import ru.kolesnikov.simplechat.model.User;

import java.util.List;

@Repository
public interface AuthRepository extends JpaRepository<AccessModel, Long> {

    boolean existsAccessModelByLogin(User userByLogin);

    AccessModel findByLogin(User login);

    List<AccessModel> findAll();
}
