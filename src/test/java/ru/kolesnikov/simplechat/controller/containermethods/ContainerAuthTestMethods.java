package ru.kolesnikov.simplechat.controller.containermethods;

import io.restassured.response.ValidatableResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import ru.kolesnikov.simplechat.controller.containermethods.dto.TestUserDTOAuth;
import ru.kolesnikov.simplechat.utils.UriComponentsBuilderUtil;

import static io.restassured.RestAssured.given;
import static ru.kolesnikov.simplechat.security.AuthorizationFilter.AUTHORIZATION;

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

    public String checkAuthAndReturnToken(TestUserDTOAuth user) {
        return checkUserAuthorization(user).extract()
                .header("Token");
    }

    public ValidatableResponse logout(String login, String token) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, "Bearer " + token)
                .when()
                .delete(UriComponentsBuilderUtil
                        .builder()
//                        .replacePath(USERS_PATH + login + LOGOUT)
                        .replacePath(LOGOUT)
                        .toUriString())
                .then()
                .and().log()
                .all();

    }

    public ValidatableResponse getAllActiveUsers(String userLogin, String token) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, "Bearer " + token)
                .when()
                .get(UriComponentsBuilderUtil
                        .builder()
                        .replacePath(USERS_PATH + userLogin + ACTIVE_USERS)
                        .toUriString())
                .then()
                .and().log()
                .all();

    }

}
