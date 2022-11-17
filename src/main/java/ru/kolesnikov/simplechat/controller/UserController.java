package ru.kolesnikov.simplechat.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.kolesnikov.simplechat.controller.dto.UserDTORegistration;
import ru.kolesnikov.simplechat.controller.dto.UserDTOResponse;
import ru.kolesnikov.simplechat.model.User;
import ru.kolesnikov.simplechat.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/api/v1/user/{login}/allusers")
    public List<UserDTOResponse> getAllUsers(@PathVariable String login) {

        return userService.getAllUsers()
                .stream()
                .map(user ->
                        new UserDTOResponse(user.getLogin(),
                                user.getName(),
                                user.getPhotoPath()))
                .collect(Collectors.toList());
    }


    //    Registration
    @PostMapping(value = "/api/v1/user")
    public UserDTOResponse addUser(@RequestBody @Valid UserDTORegistration user) {

        userService.addUser(
                new User(user.getLogin(),
                        user.getName(),
                        user.getSurname(),
                        user.getPhotoPath(),
                        user.getPassword()
                ));
        return new UserDTOResponse(user.getLogin(),
                user.getName(),
                user.getPhotoPath());
    }


    @GetMapping(value = "/api/v1/user/{login}")
    public UserDTOResponse getUserByLogin(@PathVariable String login) {

        User user = userService.findUserByLogin(login);
        return new UserDTOResponse(user.getLogin(),
                user.getName(),
                user.getPhotoPath());
    }

    @PutMapping(value = "/api/v1/user/{login}")
    public UserDTOResponse updateUser(@PathVariable String login,
                                      @RequestBody @Valid UserDTORegistration user) {

        User userUpdated = userService.updateUser(
                login,
                new User(user.getLogin(),
                        user.getName(),
                        user.getSurname(),
                        user.getPhotoPath(),
                        user.getPassword()));
        return new UserDTOResponse(userUpdated.getLogin(),
                userUpdated.getName(),
                userUpdated.getPhotoPath());
    }

    @DeleteMapping(value = "/api/v1/user/{login}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String login) {
        userService.deleteUser(login);
    }

}
