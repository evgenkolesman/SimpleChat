package ru.kolesnikov.simplechat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kolesnikov.simplechat.exceptions.UserNotFoundException;
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
        return userRepository.findById(login).orElseThrow(UserNotFoundException::new);
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User getUser(User user) {
        return userRepository.findById(user.getLogin()).orElseThrow(UserNotFoundException::new);
    }
}
