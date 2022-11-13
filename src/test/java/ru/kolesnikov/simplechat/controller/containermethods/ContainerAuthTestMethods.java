package ru.kolesnikov.simplechat.controller.containermethods;

import io.restassured.response.ValidatableResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import ru.kolesnikov.simplechat.controller.containermethods.dto.TestUserDTOAuth;
import ru.kolesnikov.simplechat.utils.UriComponentsBuilderUtil;

import static io.restassured.RestAssured.given;

@Component
public class ContainerAuthTestMethods {

    public static final String LOGOUT = "/logout";
    private final static String AUTH_ENDPOINT = "/api/v1/auth";
    private static final String ACTIVE_USERS = "/activeUsers";
    private static final String USERS_PATH = "/api/v1/user/";

    public ValidatableResponse checkUserAuthorization(TestUserDTOAuth user) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(user)
                .when()
                .post(UriComponentsBuilderUtil
                        .builder()
                        .replacePath(AUTH_ENDPOINT)
                        .toUriString())
                .then()
                .and().log()
                .all();

    }

    public ValidatableResponse logout(String login) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(UriComponentsBuilderUtil
                        .builder()
                        .replacePath(USERS_PATH + login + LOGOUT)
                        .toUriString())
                .then()
                .and().log()
                .all();

    }

    public ValidatableResponse getAllActiveUsers(String login) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(UriComponentsBuilderUtil
                        .builder()
                        .replacePath(USERS_PATH + login + ACTIVE_USERS)
                        .toUriString())
                .then()
                .and().log()
                .all();

    }

}
