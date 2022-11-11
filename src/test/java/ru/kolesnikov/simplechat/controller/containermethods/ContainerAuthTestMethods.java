package ru.kolesnikov.simplechat.controller.containermethods;

import io.restassured.response.ValidatableResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import ru.kolesnikov.simplechat.controller.containermethods.dto.TestUserDTOAuth;
import ru.kolesnikov.simplechat.utils.UriComponentsBuilderUtil;

import static io.restassured.RestAssured.given;

@Component
public class ContainerAuthTestMethods {

    private final static String AUTH_ENDPOINT = "/api/v1/auth";

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
}
