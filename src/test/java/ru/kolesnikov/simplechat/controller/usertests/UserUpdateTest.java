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
        containerAuthTestMethods.checkUserAuthorization(new TestUserDTOAuth(user.getLogin(),
                        this.password))
                .assertThat()
                .statusCode(200);

        UserDTOResponse user = containerUserTestMethods.updateUser(
                        userRegistration.getLogin(),
                        userRegistrationUpdate).assertThat().statusCode(200)
                .extract().as(UserDTOResponse.class);
        assertThat("User name should be equals", user.getName(), equalTo(name));
        assertThat("User photoPath should be equals", user.getPhotoPath(), equalTo(photoPath));
        assertThat("User login should be equals", user.getLogin(), equalTo(login));
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
                        userRegistrationUpdate).assertThat()
                .statusCode(404)
                .extract().as(ErrorModel.class);
        assertThat("Wrong error message", errorModel.getMessage(),
                containsString(String.format("Problems with user login: %s not found", anyLogin)));
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
        containerAuthTestMethods.checkUserAuthorization(new TestUserDTOAuth(user.getLogin(), this.password))
                .assertThat()
                .statusCode(200);

        var errorModel = containerUserTestMethods.updateUser(
                        "login1",
                        userRegistrationUpdate).assertThat()
                .statusCode(404)
                .extract().as(ErrorModel.class);
        assertThat("Wrong error message", errorModel.getMessage(),
                containsString("Problems with user login: login1 not found"));
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

        containerAuthTestMethods.checkUserAuthorization(new TestUserDTOAuth(userRegistrationUpdate.getLogin(),
                        userRegistrationUpdate.getPassword()))
                .assertThat()
                .statusCode(200);

        var errorModel = containerUserTestMethods.updateUser(
                        login,
                        userRegistrationUpdate).assertThat()
                .statusCode(400)
                .extract().as(ErrorModel.class);
        assertThat("Wrong error message", errorModel.getMessage(),
                containsString(login + " was already registered"));
    }


}
