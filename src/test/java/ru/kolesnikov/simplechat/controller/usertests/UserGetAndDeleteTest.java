package ru.kolesnikov.simplechat.controller.usertests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.kolesnikov.simplechat.controller.TestAbstractIntegration;
import ru.kolesnikov.simplechat.controller.containermethods.ContainerAuthTestMethods;
import ru.kolesnikov.simplechat.controller.containermethods.ContainerUserTestMethods;
import ru.kolesnikov.simplechat.controller.containermethods.dto.TestUserDTOAuth;
import ru.kolesnikov.simplechat.controller.containermethods.dto.TestUserDTORegistration;
import ru.kolesnikov.simplechat.controller.dto.UserDTOResponse;
import ru.kolesnikov.simplechat.model.ErrorModel;
import ru.kolesnikov.simplechat.repository.AuthRepository;
import ru.kolesnikov.simplechat.repository.UserRepository;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class UserGetAndDeleteTest extends TestAbstractIntegration {

    @Autowired
    private ContainerUserTestMethods containerUserTestMethods;

    @Autowired
    private ContainerAuthTestMethods authTestMethods;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthRepository authRepository;

    @LocalServerPort
    private int port;

    private UserDTOResponse user;
    private TestUserDTORegistration userRegistration;
    private String password = "password";

    @BeforeEach
    void testDataProduce() {
        RestAssured.port = port;

        var name = "name";
        var login = "login";
        var photoPath = "photoPath";
        userRegistration = new TestUserDTORegistration(login,
                name,
                "surname",
                password
                , photoPath
        );

        user = containerUserTestMethods.addUser(userRegistration).assertThat().statusCode(200)
                .extract().as(UserDTOResponse.class);
    }

    @AfterEach
    void testDataClear() {
        authRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getAllUsers() {
        authTestMethods.checkUserAuthorization(new TestUserDTOAuth(user.getLogin(), password))
                .assertThat()
                .statusCode(200);
        List<UserDTOResponse> list = containerUserTestMethods.getAllUsers(user.getLogin())
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath().getList("", UserDTOResponse.class);
        assertThat("Bad data returned", list, equalTo(List.of(user)));
    }

    @Test
    void getUserByLogin() {
        authTestMethods.checkUserAuthorization(new TestUserDTOAuth(user.getLogin(), password))
                .assertThat()
                .statusCode(200);

        UserDTOResponse userResponse = containerUserTestMethods.getUserByLogin(user.getLogin())
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .as(UserDTOResponse.class);
        assertThat("Bad data returned", userResponse, equalTo(user));
    }

    @Test
    void getUserByWrongLogin() {
        var errorModel = containerUserTestMethods.getUserByLogin(user.getName())
                .assertThat()
                .statusCode(404)
                .extract()
                .body()
                .as(ErrorModel.class);
        assertThat("Bad data returned", errorModel.getMessage(),
                containsString(String.format("Problems with user login: %s not found", user.getName())));
    }

    @Test
    void deleteUserByLogin() {
        authTestMethods.checkUserAuthorization(new TestUserDTOAuth(user.getLogin(), password))
                .assertThat()
                .statusCode(200);
        containerUserTestMethods.deleteUser(user.getLogin())
                .assertThat()
                .statusCode(204);
    }

    @Test
    void deleteUserByBadLogin() {
        var errorModel = containerUserTestMethods.deleteUser(user.getName())
                .assertThat()
                .statusCode(404)
                .extract()
                .body()
                .as(ErrorModel.class);

        assertThat("Bad data returned", errorModel.getMessage(),
                containsString(String.format("Problems with user login: %s not found", user.getName())));

    }


}
