package ru.kolesnikov.simplechat.controller.containermethods;

import io.restassured.response.ValidatableResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import ru.kolesnikov.simplechat.controller.containermethods.dto.TestUserDTORegistration;
import ru.kolesnikov.simplechat.utils.UriComponentsBuilderUtil;

import static io.restassured.RestAssured.given;
import static ru.kolesnikov.simplechat.security.AuthorizationFilter.AUTHORIZATION;

@Component
public class ContainerUserTestMethods {

    private final static String USERS_ENDPOINT = "/api/v1/user";
    private final static String USERS_ENDPOINT_WITH_LOGIN = "/api/v1/user/%s";

    public ValidatableResponse addUser(TestUserDTORegistration model) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(model)
                .when()
                .post(UriComponentsBuilderUtil
                        .builder()
                        .replacePath(USERS_ENDPOINT)
                        .toUriString())
                .then()
                .and().log()
                .all();

    }

    public ValidatableResponse getAllUsers(String login, String token) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(UriComponentsBuilderUtil
                        .builder()
                        .replacePath(String.format(USERS_ENDPOINT_WITH_LOGIN, login) + "/allusers")
                        .toUriString())
                .then()
                .and().log()
                .all();

    }

    public ValidatableResponse getUserByLogin(String login, String token) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, "Bearer " + token)
                .header("Login", login)
                .when()
                .get(UriComponentsBuilderUtil
                        .builder()
                        .replacePath(String.format(USERS_ENDPOINT_WITH_LOGIN, login))
                        .toUriString())
                .then()
                .and().log()
                .all();

    }

    public ValidatableResponse updateUser(String login, TestUserDTORegistration model, String token) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, "Bearer " + token)
                .body(model)
                .when()
                .put(UriComponentsBuilderUtil
                        .builder()
                        .replacePath(String.format(USERS_ENDPOINT_WITH_LOGIN, login))
                        .toUriString())
                .then()
                .and().log()
                .all();

    }

    public ValidatableResponse deleteUser(String login, String token) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, "Bearer " + token)
                .when()
                .delete(UriComponentsBuilderUtil
                        .builder()
                        .replacePath(String.format(USERS_ENDPOINT_WITH_LOGIN, login))
                        .toUriString())
                .then()
                .and().log()
                .all();

    }

}
