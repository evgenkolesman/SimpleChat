package ru.kolesnikov.simplechat.repository;

import lombok.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.kolesnikov.simplechat.model.AccessModel;

import java.util.List;

@Repository
public interface AuthRepository extends MongoRepository<AccessModel, Long> {

    boolean existsAccessModelByLogin(String login);

    @NonNull List<AccessModel> findAll();

    void deleteAll();

    void deleteByLogin(String login);
}
