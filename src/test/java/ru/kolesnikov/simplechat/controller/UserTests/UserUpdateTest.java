package ru.kolesnikov.simplechat.controller.UserTests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.kolesnikov.simplechat.controller.container.ContainerUserTestMethods;
import ru.kolesnikov.simplechat.controller.container.TestAbstractIntegration;
import ru.kolesnikov.simplechat.controller.container.dto.TestUserDTORegistration;
import ru.kolesnikov.simplechat.controller.dto.UserDTOResponse;
import ru.kolesnikov.simplechat.model.ErrorModel;
import ru.kolesnikov.simplechat.repository.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UserUpdateTest extends TestAbstractIntegration {

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

    private UserDTOResponse user;
    private TestUserDTORegistration userRegistration;

    @BeforeEach
    void testDataProduce() {
        RestAssured.port = port;

        var name = "name";
        var login = "login";
        var photoPath = "photoPath";
        userRegistration = new TestUserDTORegistration(login,
                name,
                "surname",
                "password"
                , photoPath
        );

        user = containerUserTestMethods.addUser(userRegistration).assertThat().statusCode(200)
                .extract().as(UserDTOResponse.class);
    }

    @AfterEach
    void testDataClear() {
        userRepository.deleteAll();
    }

    @Test
    void addUserCorrectTest() {
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

        UserDTOResponse user = containerUserTestMethods.updateUser(
                        userRegistration.getLogin(),
                        userRegistrationUpdate).assertThat().statusCode(200)
                .extract().as(UserDTOResponse.class);
        assertThat("User name should be equals", user.getName(), equalTo(name));
        assertThat("User photoPath should be equals", user.getPhotoPath(), equalTo(photoPath));
        assertThat("User login should be equals", user.getLogin(), equalTo(login));
    }


//    @Test
//    void addUserEmptyLoginTest() {
//        var name = "name";
//        var login = "";
//        var photoPath = "photoPath";
//        var errorModel = containerUserTestMethods.addUser(new TestUserDTORegistration(login,
//                        name,
//                        "surname",
//                        "password"
//                        , photoPath
//                )).assertThat().statusCode(400)
//                .extract().response()
//                .as(ErrorModel.class);
//
//        assertThat("Wrong error message", errorModel.getMessage(), equalTo(LOGIN_EMPTY_MESSAGE));
//
//    }


}
