package ru.kolesnikov.simplechat.controller.authtests;

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
import ru.kolesnikov.simplechat.repository.AuthRepository;
import ru.kolesnikov.simplechat.repository.UserRepository;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AuthorizationTests extends TestAbstractIntegration {


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
        authRepository.deleteAll();
    }

    @Test
    void checkUserAuthorizationCorrect() {
        var result = containerAuthTestMethods
                .checkUserAuthorization(new TestUserDTOAuth(userRegistration.getLogin(),
                        userRegistration.getPassword()))
                .assertThat()
                .statusCode(200)
                .extract()
                .response();
        assertThat("Bad return value", result, notNullValue());

    }

    @Test
    void checkUserAuthorizationNotCorrectLogin() {
        var result = containerAuthTestMethods
                .checkUserAuthorization(new TestUserDTOAuth(userRegistration.getLogin() + 1,
                        userRegistration.getPassword()))
                .assertThat()
                .statusCode(403);
    }

    @Test
    void checkUserAuthorizationNotCorrectPassword() {
        var result = containerAuthTestMethods
                .checkUserAuthorization(new TestUserDTOAuth(userRegistration.getLogin(),
                        userRegistration.getPassword() + 1))
                .assertThat()
                .statusCode(403)
                .extract()
                .response();
        assertThat("Bad return value", result.getHeader("Authorization"), nullValue());
    }


    @Test
    void checkUserAuthorizationNotCorrectPasswordNull() {
        var response = containerAuthTestMethods
                .checkUserAuthorization(new TestUserDTOAuth(userRegistration.getLogin(),
                        null))
                .assertThat()
                .statusCode(403)
                .extract()
                .response();
        assertThat("Bad return value", response.getHeader("Authorization"), nullValue());

    }

    @Test
    void checkUserAuthorizationNotCorrectPasswordEmpty() {
        var response = containerAuthTestMethods
                .checkUserAuthorization(new TestUserDTOAuth(userRegistration.getLogin(),
                        ""))
                .assertThat()
                .statusCode(403)
                .extract()
                .response();
        assertThat("Bad return value", response.getHeader("Authorization"), nullValue());
    }

    @Test
    void checkUserAuthorizationNotCorrectLoginNull() {
        var response = containerAuthTestMethods
                .checkUserAuthorization(new TestUserDTOAuth(null,
                        userRegistration.getPassword()))
                .assertThat()
                .statusCode(403)
                .extract()
                .response();
        assertThat("Bad return value", response.getHeader("Authorization"), nullValue());

    }

    @Test
    void checkUserAuthorizationNotCorrectLoginEmpty() {
        var response = containerAuthTestMethods
                .checkUserAuthorization(new TestUserDTOAuth("",
                        userRegistration.getPassword()))
                .assertThat()
                .statusCode(403)
                .extract()
                .response();
        assertThat("Bad return value", response.getHeader("Authorization"), nullValue());
    }

    @Test
    void checkLogoutOnceAndSecondStillGetPermission() {
        String token = containerAuthTestMethods
                .checkAuthAndReturnToken(new TestUserDTOAuth(userRegistration.getLogin(),
                        userRegistration.getPassword()));
        TestUserDTORegistration userRegistration1 = new TestUserDTORegistration(userRegistration.getLogin() + 1,
                userRegistration.getName() + 1,
                userRegistration.getSurname() + 1,
                userRegistration.getPassword() + 1
                , ""
        );

        containerUserTestMethods.addUser(userRegistration1).assertThat().statusCode(200)
                .extract().as(UserDTOResponse.class);

        String token1 = containerAuthTestMethods
                .checkAuthAndReturnToken(new TestUserDTOAuth(userRegistration1.getLogin(),
                        userRegistration1.getPassword()));


        containerAuthTestMethods.logout(userRegistration.getLogin(), token)
                .assertThat()
                .statusCode(200);

        var list = containerAuthTestMethods.getAllActiveUsers(userRegistration1.getLogin(), token1)
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("", String.class);
        assertThat("List of active users is wrong",
                list, equalTo(List.of(userRegistration1.getLogin())));

        containerAuthTestMethods.logout(userRegistration1.getLogin(), token1)
                .assertThat()
                .statusCode(200);

    }


    @Test
    void checkLogout() {
        String token = containerAuthTestMethods
                .checkAuthAndReturnToken(new TestUserDTOAuth(userRegistration.getLogin(),
                        userRegistration.getPassword()));
        containerAuthTestMethods.logout(userRegistration.getLogin(), token)
                .assertThat()
                .statusCode(200);


    }

    @Test
    void checkGetAllActiveUsers() {
        String token = containerAuthTestMethods
                .checkAuthAndReturnToken(new TestUserDTOAuth(userRegistration.getLogin(),
                        userRegistration.getPassword()));

        var list = containerAuthTestMethods.getAllActiveUsers(user.getLogin(), token)
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("", String.class);
        assertThat("List of active users is wrong",
                list, equalTo(List.of(user.getLogin())));
    }

    @Test
    void checkGetAllActiveWithoutAuthUsers() {
        containerAuthTestMethods.getAllActiveUsers(user.getLogin(), user.getLogin())
                .assertThat()
                .statusCode(403);
    }

     @Test
    void checkGetAllActiveWithBadLoginAuthUsers() {
         String token = containerAuthTestMethods
                 .checkAuthAndReturnToken(new TestUserDTOAuth(userRegistration.getLogin(),
                         userRegistration.getPassword()));

         containerAuthTestMethods.getAllActiveUsers(user.getName(), token)
                .assertThat()
                .statusCode(451);
    }


}
