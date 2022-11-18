package ru.kolesnikov.simplechat.controller.usertests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private Environment environment;

    @LocalServerPort
    private int port;

    private UserDTOResponse user;
    private final String password = "password";

    @BeforeEach
    void testDataProduce() {
        RestAssured.port = port;

        var name = "name";
        var login = "login";
        var photoPath = "photoPath";
        TestUserDTORegistration userRegistration = new TestUserDTORegistration(login,
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
        userRepository.deleteAll();
        authRepository.deleteAll();
    }

    @Test
    void getAllUsers() {
        String token = authTestMethods.checkAuthAndReturnToken(new TestUserDTOAuth(user.login(), password));
        List<UserDTOResponse> list = containerUserTestMethods.getAllUsers(user.login(), token)
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath().getList("", UserDTOResponse.class);
        assertThat("Bad data returned", list, equalTo(List.of(user)));
    }

    @Test
    void getUserByLogin() {
        var token = authTestMethods.checkAuthAndReturnToken(new TestUserDTOAuth(user.login(), password));

        UserDTOResponse userResponse = containerUserTestMethods.getUserByLogin(user.login(), token)
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .as(UserDTOResponse.class);
        assertThat("Bad data returned", userResponse, equalTo(user));
    }

    @Test
    void getUserByWrongLogin() {
        var response = containerUserTestMethods.getUserByLogin(user.name(), "Ds")
                .assertThat()
                .statusCode(403)
                .extract()
                .as(ErrorModel.class);
        assertThat("Bad data returned", response.getMessage(),
                equalTo("Wrong token"));
    }

    @Test
    void deleteUserByLogin() {
        String token = authTestMethods.checkAuthAndReturnToken(new TestUserDTOAuth(user.login(), password));
        containerUserTestMethods.deleteUser(user.login(), token)
                .assertThat()
                .statusCode(204);
    }

    @Test
    void deleteUserByBadLogin() {
        String token = authTestMethods.checkAuthAndReturnToken(new TestUserDTOAuth(user.login(), password));

        var errorModel = containerUserTestMethods.deleteUser(user.name(), token)
                .assertThat()
                .statusCode(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.value())
                .extract()
                .as(ErrorModel.class);

        assertThat("Bad data returned", errorModel.getMessage(),
                equalTo(environment.getProperty("exceptions.notEnoughPermissions")));

    }


}
