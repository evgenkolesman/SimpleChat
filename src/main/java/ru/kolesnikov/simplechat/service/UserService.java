package ru.kolesnikov.simplechat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kolesnikov.simplechat.exceptions.UserNotFoundException;
import ru.kolesnikov.simplechat.exceptions.UserWasRegisteredException;
import ru.kolesnikov.simplechat.model.User;
import ru.kolesnikov.simplechat.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

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

    public User addUser(User user) {
        if (findUserByLoginPresent(user.getLogin())) {
            throw new UserWasRegisteredException(user.getLogin());
        }
        return userRepository.save(user);
    }


    public User updateUser(String login, User user) {

        if (findUserByLoginPresent(user.getLogin())
                && findUserByLogin(login)
                .equals(findUserByLogin(user.getLogin()))) {
            throw new UserWasRegisteredException(user.getLogin());
        }

        return userRepository.save(user);
    }

    public void deleteUser(String login) {
        userRepository.delete(findUserByLogin(login));
    }

    public boolean checkAuthorization(String login) {
        return userRepository
                .findUserByLogin(
                        login)
                .isPresent();
    }


    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User userEntity = findUserByLogin(login);
        if (userEntity == null) {
            throw new NullPointerException();
        }
        return new org.springframework.security.core.userdetails.User(userEntity.getLogin(), userEntity.getPassword(),
                true, true, true, true, new ArrayList<>());
    }
}
