package ru.kolesnikov.simplechat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kolesnikov.simplechat.exceptions.NotAuthorizedException;
import ru.kolesnikov.simplechat.model.AccessModel;
import ru.kolesnikov.simplechat.repository.AuthRepository;
import ru.kolesnikov.simplechat.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    public void authorize(String login) {
        if (!checkAccess(login)) {
            if (userRepository.existsById(login)) {
                authRepository.save(new AccessModel(login));
            } else {
                checkNotAuthorized(login);
            }
        } else if (checkAccess((login))) {
            log.info("this user was already authenticated with such login " + login);
        }

    }

    public void logout(String login) {
        authRepository.deleteByLogin(login);
    }

    public boolean checkAccess(String login) {
        return authRepository.existsAccessModelByLogin(login);
    }

    public List<String> getAllActiveLogins() {
        return authRepository.findAll()
                .stream().map(AccessModel::getLogin)
                .collect(Collectors.toList());

    }

    public void checkNotAuthorized(String login) {
        if (!checkAccess(login)) {
            throw new NotAuthorizedException();
        }
    }
}
