package ru.kolesnikov.simplechat.controller.authtests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.kolesnikov.simplechat.controller.containermethods.ContainerAuthTestMethods;
import ru.kolesnikov.simplechat.controller.containermethods.ContainerUserTestMethods;
import ru.kolesnikov.simplechat.controller.TestAbstractIntegration;
import ru.kolesnikov.simplechat.controller.containermethods.dto.TestUserDTOAuth;
import ru.kolesnikov.simplechat.controller.containermethods.dto.TestUserDTORegistration;
import ru.kolesnikov.simplechat.controller.dto.UserDTOResponse;
import ru.kolesnikov.simplechat.model.ErrorModel;
import ru.kolesnikov.simplechat.repository.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class AuthorizationTests extends TestAbstractIntegration {


    @Autowired
    private ContainerUserTestMethods containerUserTestMethods;

    @Autowired
    private ContainerAuthTestMethods containerAuthTestMethods;


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
    void checkUserAuthorizationCorrect() {
        Boolean result = containerAuthTestMethods
                .checkUserAuthorization(new TestUserDTOAuth(userRegistration.getLogin(),
                        userRegistration.getPassword()))
                .assertThat()
                .statusCode(200)
                .extract()
                .as(Boolean.class);
        assertThat("Bad return value", true, is(result));

    }

    @Test
    void checkUserAuthorizationNotCorrectLogin() {
        Boolean result = containerAuthTestMethods
                .checkUserAuthorization(new TestUserDTOAuth(userRegistration.getLogin() + 1,
                        userRegistration.getPassword()))
                .assertThat()
                .statusCode(200)
                .extract()
                .as(Boolean.class);
        assertThat("Bad return value", false, is(result));
    }

    @Test
    void checkUserAuthorizationNotCorrectPassword() {
        Boolean result = containerAuthTestMethods
                .checkUserAuthorization(new TestUserDTOAuth(userRegistration.getLogin(),
                        userRegistration.getPassword() + 1))
                .assertThat()
                .statusCode(200)
                .extract()
                .as(Boolean.class);
        assertThat("Bad return value", false, is(result));
    }


    @Test
    void checkUserAuthorizationNotCorrectPasswordNull() {
        var errorModel = containerAuthTestMethods
                .checkUserAuthorization(new TestUserDTOAuth(userRegistration.getLogin(),
                        null))
                .assertThat()
                .statusCode(400)
                .extract()
                .as(ErrorModel.class);
        assertThat("Bad return value", errorModel.getMessage(),
                equalTo("Invalid data: password field should be filled"));

    }

    @Test
    void checkUserAuthorizationNotCorrectPasswordEmpty() {
        var errorModel = containerAuthTestMethods
                .checkUserAuthorization(new TestUserDTOAuth(userRegistration.getLogin(),
                        ""))
                .assertThat()
                .statusCode(400)
                .extract()
                .as(ErrorModel.class);
        assertThat("Bad return value", errorModel.getMessage(),
                equalTo("Invalid data: password must be minimum 8 characters long"));
    }

@Test
    void checkUserAuthorizationNotCorrectLoginNull() {
        var errorModel = containerAuthTestMethods
                .checkUserAuthorization(new TestUserDTOAuth(null,
                        userRegistration.getPassword()))
                .assertThat()
                .statusCode(400)
                .extract()
                .as(ErrorModel.class);
        assertThat("Bad return value", errorModel.getMessage(),
                equalTo("Invalid data: login field should be filled"));

    }

    @Test
    void checkUserAuthorizationNotCorrectLoginEmpty() {
        var errorModel = containerAuthTestMethods
                .checkUserAuthorization(new TestUserDTOAuth("",
                        userRegistration.getPassword()))
                .assertThat()
                .statusCode(400)
                .extract()
                .as(ErrorModel.class);
        assertThat("Bad return value", errorModel.getMessage(),
                equalTo("Invalid data: login must be minimum 2 characters long"));
    }
}
