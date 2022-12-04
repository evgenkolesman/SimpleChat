package ru.kolesnikov.simplechat.model;

import org.mapstruct.*;
import ru.kolesnikov.simplechat.controller.dto.UserDTORegistration;
import ru.kolesnikov.simplechat.controller.dto.UserDTOResponse;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy =
        NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    @Mapping(source = "login", target = "login")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "surname", target = "surname")
    @Mapping(source = "photoPath", target = "photoPath")
    @Mapping(source = "password", target = "password")
    public User userFromUserDTORegistration(UserDTORegistration user);


    @Mapping(target = "login", source = "login")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "photoPath", source = "photoPath")
    public UserDTOResponse userDTOResponseFromUser(UserDTORegistration user);


    @Mapping(target = "login", source = "login")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "photoPath", source = "photoPath")
    public UserDTOResponse userDTOResponseFromUser(User user);
}
