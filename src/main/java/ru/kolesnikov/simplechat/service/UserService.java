package ru.kolesnikov.simplechat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kolesnikov.simplechat.controller.dto.UserDTOAuth;
import ru.kolesnikov.simplechat.exceptions.UserNotFoundException;
import ru.kolesnikov.simplechat.exceptions.UserWasRegisteredException;
import ru.kolesnikov.simplechat.model.User;
import ru.kolesnikov.simplechat.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findUserByLogin(String login) {
        return userRepository
                .findById(login)
                .orElseThrow(() -> new UserNotFoundException(login));
    }

    public boolean findUserByLoginPresent(String login) {
        return userRepository
                .findById(login)
                .isPresent();
    }

    public User getUser(User user) {
        String login = user.getLogin();
        return userRepository
                .findById(login)
                .orElseThrow(() -> new UserNotFoundException(login));
    }

    public User addUser(User user) {
        if (findUserByLoginPresent(user.getLogin())) {
            throw new UserWasRegisteredException(user.getLogin());
        }
        return userRepository.save(user);
    }


    public User updateUser(String login, User user) {
        if (!findUserByLoginPresent(login)) {
            throw new UserNotFoundException(user.getLogin());
        }
         if (findUserByLoginPresent(user.getLogin())
         && findUserByLogin(login)
                 .equals(findUserByLogin(user.getLogin()))) {
            throw new UserNotFoundException(user.getLogin());
        }

        return userRepository.save(user);
    }

       public void deleteUser(String login) {
           userRepository.delete(findUserByLogin(login));
       }

    public boolean checkAuthorization(UserDTOAuth user) {
        return userRepository
                .findUserByLoginAndPassword(
                        user.getLogin(),
                        user.getPassword())
                .isPresent();
    }
}
