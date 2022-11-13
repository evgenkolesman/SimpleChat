package ru.kolesnikov.simplechat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kolesnikov.simplechat.model.AccessModel;
import ru.kolesnikov.simplechat.repository.AuthRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final UserService userService;

    public void authorize(String login) {
        authRepository.save(new AccessModel(userService.findUserByLogin(login)));
    }

    public void logout(String login) {
        authRepository.delete(authRepository.findByLogin(userService.findUserByLogin(login)));
    }

    public boolean checkAccess(String login) {
        return authRepository.existsAccessModelByLogin(userService.findUserByLogin(login));
    }

    public List<String> getAllActiveLogins() {
        return authRepository.findAll()
                .stream().map(accessModel -> accessModel.getLogin().getLogin())
                .collect(Collectors.toList());

    }
}
