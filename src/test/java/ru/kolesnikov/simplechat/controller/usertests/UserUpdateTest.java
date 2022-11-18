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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class UserUpdateTest extends TestAbstractIntegration {

    @Autowired
    private ContainerUserTestMethods containerUserTestMethods;
    @Autowired
    private ContainerAuthTestMethods containerAuthTestMethods;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private Environment environment;

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
    void updateUserCorrectTest() {
        var name = "name1";
        var login = "login1";
        var surname = "surname1";
        var photoPath = "photoPath1";
        var password = "password1";
        TestUserDTORegistration userRegistrationUpdate = new TestUserDTORegistration(
                login,
                name,
                surname,
                password,
                photoPath
        );
        String token = containerAuthTestMethods.checkAuthAndReturnToken(new TestUserDTOAuth(user.login(),
                this.password));

        UserDTOResponse user = containerUserTestMethods.updateUser(
                        this.user.login(),
                        userRegistrationUpdate,
                        token).assertThat().statusCode(200)
                .extract().as(UserDTOResponse.class);
        assertThat("User name should be equals", user.name(), equalTo(name));
        assertThat("User photoPath should be equals", user.photoPath(), equalTo(photoPath));
        assertThat("User login should be equals", user.login(), equalTo(login));
    }


    @Test
    void updateUserBadLoginTest() {
        var name = "name";
        var login = "login1";
        var surname = "surname1";
        var photoPath = "photoPath1";
        var password = "password1";
        TestUserDTORegistration userRegistrationUpdate = new TestUserDTORegistration(
                login,
                name,
                surname,
                password,
                photoPath
        );

        String anyLogin = "anyLogin";
        var errorModel = containerUserTestMethods.updateUser(
                        anyLogin,
                        userRegistrationUpdate, anyLogin).assertThat()
                .statusCode(403)
                .extract().as(ErrorModel.class);
        assertThat("Bad data returned", errorModel.getMessage(),
                equalTo("Wrong token"));
    }

    @Test
    void updateUserBadLoginBothTest() {
        var name = "name";
        var login = "login1";
        var surname = "surname1";
        var photoPath = "photoPath1";
        var password = "password1";
        TestUserDTORegistration userRegistrationUpdate = new TestUserDTORegistration(
                login,
                name,
                surname,
                password,
                photoPath
        );
        String token =
                containerAuthTestMethods.checkAuthAndReturnToken(new TestUserDTOAuth(user.login(), this.password));

        var errorModel = containerUserTestMethods.updateUser(
                        "login10",
                        userRegistrationUpdate,
                        token).assertThat()
                .statusCode(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.value())
                .extract().as(ErrorModel.class);
        assertThat("Bad data returned", errorModel.getMessage(),
                equalTo(environment.getProperty("exceptions.notEnoughPermissions")));
    }


    @Test
    void updateUserBadLoginWasRegisteredTest() {
        var name = "name";
        var login = "login1";
        var surname = "surname1";
        var photoPath = "photoPath1";
        var password = "password1";
        TestUserDTORegistration userRegistrationUpdate = new TestUserDTORegistration(
                login,
                name,
                surname,
                password,
                photoPath
        );


        user = containerUserTestMethods.addUser(userRegistrationUpdate)
                .assertThat().statusCode(200)
                .extract().as(UserDTOResponse.class);

        String token = containerAuthTestMethods.checkAuthAndReturnToken(new TestUserDTOAuth(userRegistrationUpdate.getLogin(),
                userRegistrationUpdate.getPassword()));

        var errorModel = containerUserTestMethods.updateUser(
                        login,
                        userRegistrationUpdate,
                        token).assertThat()
                .statusCode(400)
                .extract().as(ErrorModel.class);
        assertThat("Wrong error message", errorModel.getMessage(),
                containsString(login + " was already registered"));
    }


}
