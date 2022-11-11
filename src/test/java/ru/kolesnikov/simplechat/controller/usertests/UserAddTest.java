package ru.kolesnikov.simplechat.controller.usertests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.kolesnikov.simplechat.controller.containermethods.ContainerUserTestMethods;
import ru.kolesnikov.simplechat.controller.TestAbstractIntegration;
import ru.kolesnikov.simplechat.controller.containermethods.dto.TestUserDTORegistration;
import ru.kolesnikov.simplechat.controller.dto.UserDTOResponse;
import ru.kolesnikov.simplechat.model.ErrorModel;
import ru.kolesnikov.simplechat.repository.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class UserAddTest extends TestAbstractIntegration {

    private static final String LOGIN_EMPTY_MESSAGE = "Invalid data: login must be minimum 2 characters long";
    private static final String NAME_EMPTY_MESSAGE = "Invalid data: name must be minimum 2 characters long";
    private static final String SURNAME_EMPTY_MESSAGE = "Invalid data: surname must be minimum 2 characters long";
    private static final String PASSWORD_EMPTY_MESSAGE = "Invalid data: password must be minimum 8 characters long";

    private static final String PASSWORD_NULL_MESSAGE = "Invalid data: password field should be filled";
    private static final String NAME_NULL_MESSAGE = "Invalid data: name field should be filled";
    private static final String SURNAME_NULL_MESSAGE = "Invalid data: surname field should be filled";
    private static final String LOGIN_NULL_MESSAGE = "Invalid data: login field should be filled";

    @Autowired
    private ContainerUserTestMethods containerUserTestMethods;
    @Autowired
    private UserRepository userRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void testDataProduce() {
        RestAssured.port = port;
    }

    @AfterEach
    void testDataClear() {
        userRepository.deleteAll();
    }

    @Test
    void addUserCorrectTest() {
        var name = "name";
        var login = "login";
        var photoPath = "photoPath";
        UserDTOResponse user = containerUserTestMethods.addUser(new TestUserDTORegistration(login,
                        name,
                        "surname",
                        "password"
                        , photoPath
                )).assertThat().statusCode(200)
                .extract().as(UserDTOResponse.class);
        assertThat("User name should be equals", user.getName(), equalTo(name));
        assertThat("User photoPath should be equals", user.getPhotoPath(), equalTo(photoPath));
        assertThat("User login should be equals", user.getLogin(), equalTo(login));
    }

    @Test
    void addUserCorrectWithEmptyPhotoPathTest() {
        var name = "name";
        var login = "login";
        var photoPath = "";
        UserDTOResponse user = containerUserTestMethods.addUser(new TestUserDTORegistration(login,
                        name,
                        "surname",
                        "password"
                        , photoPath
                )).assertThat().statusCode(200)
                .extract().as(UserDTOResponse.class);
        assertThat("User name should be equals", user.getName(), equalTo(name));
        assertThat("User photoPath should be equals", user.getPhotoPath(), equalTo(photoPath));
        assertThat("User login should be equals", user.getLogin(), equalTo(login));
    }

    @Test
    void addUserCorrectWithEmptyPhotoPathDoubleTest() {
        var name = "name";
        var login = "login";
        var photoPath = "";
        UserDTOResponse user = containerUserTestMethods.addUser(new TestUserDTORegistration(login,
                        name,
                        "surname",
                        "password"
                        , photoPath
                )).assertThat().statusCode(200)
                .extract().as(UserDTOResponse.class);
        var errorModel = containerUserTestMethods.addUser(new TestUserDTORegistration(login,
                        name,
                        "surname",
                        "password"
                        , photoPath
                )).assertThat().statusCode(400)
                .extract().as(ErrorModel.class);

        assertThat("Wrong error message", errorModel.getMessage(),
                containsString("login was already registered"));
    }

    @Test
    void addUserEmptyLoginTest() {
        var name = "name";
        var login = "";
        var photoPath = "photoPath";
        var errorModel = containerUserTestMethods.addUser(new TestUserDTORegistration(login,
                        name,
                        "surname",
                        "password"
                        , photoPath
                )).assertThat().statusCode(400)
                .extract().response()
                .as(ErrorModel.class);

        assertThat("Wrong error message", errorModel.getMessage(), equalTo(LOGIN_EMPTY_MESSAGE));

    }

    @Test
    void addUserEmptyNameTest() {
        var name = "";
        var login = "login";
        var photoPath = "photoPath";
        var errorModel = containerUserTestMethods.addUser(new TestUserDTORegistration(login,
                        name,
                        "surname",
                        "password"
                        , photoPath
                )).assertThat().statusCode(400)
                .extract()
                .as(ErrorModel.class);
        assertThat("Wrong error message", errorModel.getMessage(), equalTo(NAME_EMPTY_MESSAGE));

    }

    @Test
    void addUserEmptyPasswordTest() {
        var name = "name";
        var login = "login";
        var photoPath = "photoPath";
        var errorModel = containerUserTestMethods.addUser(new TestUserDTORegistration(login,
                        name,
                        "surname",
                        ""
                        , photoPath
                )).assertThat().statusCode(400)
                .extract().as(ErrorModel.class);
        assertThat("Wrong error message", errorModel.getMessage(), equalTo(PASSWORD_EMPTY_MESSAGE));

    }

    @Test
    void addUserEmptySurnameTest() {
        var name = "name";
        var login = "login";
        var surname = "";
        var photoPath = "photoPath";

        var errorModel = containerUserTestMethods.addUser(new TestUserDTORegistration(login,
                        name,
                        surname,
                        "password"
                        , photoPath
                )).assertThat().statusCode(400)
                .extract().as(ErrorModel.class);

        assertThat("Wrong error message", errorModel.getMessage(), equalTo(SURNAME_EMPTY_MESSAGE));

    }

    @Test
    void addUserNullLoginTest() {
        var name = "name";
        String login = null;
        var photoPath = "photoPath";
        var errorModel = containerUserTestMethods.addUser(new TestUserDTORegistration(login,
                        name,
                        "surname",
                        "password"
                        , photoPath
                )).assertThat().statusCode(400)
                .extract().response()
                .as(ErrorModel.class);

        assertThat("Wrong error message", errorModel.getMessage(), equalTo(LOGIN_NULL_MESSAGE));

    }

    @Test
    void addUserNullNameTest() {
        String name = null;
        var login = "login";
        var photoPath = "photoPath";
        var errorModel = containerUserTestMethods.addUser(new TestUserDTORegistration(login,
                        name,
                        "surname",
                        "password"
                        , photoPath
                )).assertThat().statusCode(400)
                .extract()
                .as(ErrorModel.class);
        assertThat("Wrong error message", errorModel.getMessage(), equalTo(NAME_NULL_MESSAGE));

    }

    @Test
    void addUserNullPasswordTest() {
        var name = "name";
        var login = "login";
        var photoPath = "photoPath";
        var errorModel = containerUserTestMethods.addUser(new TestUserDTORegistration(login,
                        name,
                        "surname",
                        null
                        , photoPath
                )).assertThat().statusCode(400)
                .extract().as(ErrorModel.class);
        assertThat("Wrong error message", errorModel.getMessage(), equalTo(PASSWORD_NULL_MESSAGE));

    }

    @Test
    void addUserNullSurnameTest() {
        var name = "name";
        var login = "login";
        String surname = null;
        var photoPath = "photoPath";

        var errorModel = containerUserTestMethods.addUser(new TestUserDTORegistration(login,
                        name,
                        surname,
                        "password"
                        , photoPath
                )).assertThat().statusCode(400)
                .extract().as(ErrorModel.class);
        assertThat("Wrong error message", errorModel.getMessage(), equalTo(SURNAME_NULL_MESSAGE));

    }
}
