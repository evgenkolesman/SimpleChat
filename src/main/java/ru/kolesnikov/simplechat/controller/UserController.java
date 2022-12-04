package ru.kolesnikov.simplechat.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.kolesnikov.simplechat.controller.dto.UserDTORegistration;
import ru.kolesnikov.simplechat.controller.dto.UserDTOResponse;
import ru.kolesnikov.simplechat.model.User;
import ru.kolesnikov.simplechat.model.UserMapper;
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
    private final UserMapper userMapper;

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
        User userFromDTO = userMapper.userFromUserDTORegistration(user);
        userService.addUser(userFromDTO);
//        userService.addUser(
//                new User(user.login(),
//                        user.name(),
//                        user.surname(),
//                        user.photoPath(),
//                        user.password()
//                ));
//        return new UserDTOResponse(user.login(),
//                user.name(),
//                user.photoPath());
        return userMapper.userDTOResponseFromUser(user);
    }


    @GetMapping(value = "/api/v1/user/{login}")
    public UserDTOResponse getUserByLogin(@PathVariable String login) {

        User user = userService.findUserByLogin(login);
//        return new UserDTOResponse(user.getLogin(),
//                user.getName(),
//                user.getPhotoPath());
        return userMapper.userDTOResponseFromUser(user);
    }

    @PutMapping(value = "/api/v1/user/{login}")
    public UserDTOResponse updateUser(@PathVariable String login,
                                      @RequestBody @Valid UserDTORegistration user) {

        User userUpdated = userService.updateUser(
                login,
                new User(user.login(),
                        user.name(),
                        user.surname(),
                        user.photoPath(),
                        user.password()));
//        return new UserDTOResponse(userUpdated.getLogin(),
//                userUpdated.getName(),
//                userUpdated.getPhotoPath());
        return userMapper.userDTOResponseFromUser(userUpdated);

    }

    @DeleteMapping(value = "/api/v1/user/{login}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String login) {
        userService.deleteUser(login);
    }
}
