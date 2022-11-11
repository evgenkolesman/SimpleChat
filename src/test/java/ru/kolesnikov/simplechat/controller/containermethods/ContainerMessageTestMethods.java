package ru.kolesnikov.simplechat.controller.containermethods;

import io.restassured.response.ValidatableResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.kolesnikov.simplechat.controller.dto.MessageDTORequest;
import ru.kolesnikov.simplechat.utils.UriComponentsBuilderUtil;

import static io.restassured.RestAssured.given;

@Component
public class ContainerMessageTestMethods {

    private final static String MESSAGES_ENDPOINT_WITH_LOGIN = "/api/v1/user/%s/messages";
    private final static String MESSAGES_ENDPOINT_WITH_LOGIN_AND_ID = "/api/v1/user/%s/messages/%s";
    private final static String MESSAGES_ENDPOINT_ALL_MESSAGES = "/api/v1/allmessages";


    public ValidatableResponse addMessage(String login,
                                          MessageDTORequest message) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(message)
                .when()
                .post(UriComponentsBuilderUtil
                        .builder()
                        .replacePath(String.format(MESSAGES_ENDPOINT_WITH_LOGIN, login))
                        .toUriString())
                .then()
                .and().log()
                .all();

    }

    public ValidatableResponse getAllMessagesWithLogin(String login) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(UriComponentsBuilderUtil
                        .builder()
                        .replacePath(String.format(MESSAGES_ENDPOINT_WITH_LOGIN, login))
                        .toUriString())
                .then()
                .and().log()
                .all();

    }

    public ValidatableResponse updateMessage(String login,
                                             String id,
                                             MessageDTORequest message) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(message)
                .when()
                .put(UriComponentsBuilderUtil
                        .builder()
                        .replacePath(String.format(MESSAGES_ENDPOINT_WITH_LOGIN_AND_ID, login, id))
                        .toUriString())
                .then()
                .and().log()
                .all();

    }

    public ValidatableResponse getMessageById(String login,
                                              String id) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(UriComponentsBuilderUtil
                        .builder()
                        .replacePath(String.format(MESSAGES_ENDPOINT_WITH_LOGIN_AND_ID, login, id))
                        .toUriString())
                .then()
                .and().log()
                .all();

    }

    public ValidatableResponse deleteMessage(String login,
                                             String id) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(UriComponentsBuilderUtil
                        .builder()
                        .replacePath(String.format(MESSAGES_ENDPOINT_WITH_LOGIN_AND_ID, login, id))
                        .toUriString())
                .then()
                .and().log()
                .all();

    }

    public ValidatableResponse getAllMessages(String dateStart,
                                             String dateEnd) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.addIfAbsent("dateStart", dateStart);
        params.addIfAbsent("dateEnd", dateEnd);
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(UriComponentsBuilderUtil
                        .builder()
                        .replacePath(MESSAGES_ENDPOINT_ALL_MESSAGES)
                        .replaceQueryParams(params)
                        .toUriString())
                .then()
                .and().log()
                .all();

    }


}

