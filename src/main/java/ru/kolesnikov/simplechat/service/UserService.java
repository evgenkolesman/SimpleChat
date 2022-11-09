package ru.kolesnikov.simplechat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kolesnikov.simplechat.exceptions.UserNotFoundException;
import ru.kolesnikov.simplechat.model.Message;
import ru.kolesnikov.simplechat.model.User;
import ru.kolesnikov.simplechat.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User findUserByLogin(String login) {
        return userRepository.findById(login).orElseThrow(() -> new UserNotFoundException());
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }
}
