package ru.kolesnikov.simplechat.controller.container;

import io.restassured.response.ValidatableResponse;
import org.springframework.http.MediaType;
import ru.kolesnikov.simplechat.controller.dto.UserDTOAuth;

import static io.restassured.RestAssured.given;

public class AbstractContainerUserMethods {



    public ValidatableResponse addUser(String login, String password) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new UserDTOAuth(login, password))
                .when()
                .post("/api/v1/user")
                .then()
                .and().log()
                .all();


    }

}
