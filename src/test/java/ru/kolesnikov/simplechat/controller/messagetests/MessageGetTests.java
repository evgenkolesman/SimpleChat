package ru.kolesnikov.simplechat.controller.messagetests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.kolesnikov.simplechat.controller.containermethods.ContainerMessageTestMethods;
import ru.kolesnikov.simplechat.controller.containermethods.ContainerUserTestMethods;
import ru.kolesnikov.simplechat.controller.TestAbstractIntegration;
import ru.kolesnikov.simplechat.controller.containermethods.dto.TestUserDTORegistration;
import ru.kolesnikov.simplechat.controller.dto.MessageDTORequest;
import ru.kolesnikov.simplechat.controller.dto.MessageDTOResponse;
import ru.kolesnikov.simplechat.controller.dto.UserDTOResponse;
import ru.kolesnikov.simplechat.model.ErrorModel;
import ru.kolesnikov.simplechat.repository.MessageRepository;
import ru.kolesnikov.simplechat.repository.UserRepository;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class MessageGetTests extends TestAbstractIntegration {


    @Autowired
    private ContainerMessageTestMethods messageContainer;

    @Autowired
    private ContainerUserTestMethods userContainer;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @LocalServerPort
    private int port;
    private TestUserDTORegistration userRegistration;
    private UserDTOResponse user;
    private MessageDTORequest messageDTORequest;

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
        messageDTORequest = new MessageDTORequest("Message to chat");

        user = userContainer.addUser(userRegistration).assertThat().statusCode(200)
                .extract().as(UserDTOResponse.class);
    }

    @AfterEach
    void testDataClear() {
        messageRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    void getMessageByIdCorrect() {
        MessageDTOResponse message = messageContainer.addMessage(user.getLogin(), messageDTORequest)
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .as(MessageDTOResponse.class);
        var result = messageContainer.getMessageById(message.getLogin(), message.getId())
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .as(MessageDTOResponse.class);
        assertThat("Wrong getMessageById operation ",
                result, equalTo(message));

    }

    @Test
    void getMessageByIdNotCorrect() {
        MessageDTOResponse message = messageContainer.addMessage(user.getLogin(), messageDTORequest)
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .as(MessageDTOResponse.class);
        String badLogin = "dfsd";
        var errorModel = messageContainer.getMessageById(badLogin, message.getId())
                .assertThat()
                .statusCode(404)
                .extract()
                .body()
                .as(ErrorModel.class);
        assertThat("Wrong error message",
                errorModel.getMessage(), containsString(badLogin + " not found"));

    }

    @Test
    void getAllMessagesWithLoginCorrect() {
        MessageDTOResponse message = messageContainer.addMessage(user.getLogin(), messageDTORequest)
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .as(MessageDTOResponse.class);
        var result = messageContainer.getAllMessagesWithLogin(message.getLogin())
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("", MessageDTOResponse.class);
        assertThat("Wrong data returned",
                result, equalTo(List.of(message)));

    }

    @Test
    void getAllMessagesWithLoginWithBadLogin() {
        MessageDTOResponse message = messageContainer.addMessage(user.getLogin(), messageDTORequest)
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .as(MessageDTOResponse.class);
        String badLogin = "dfsd";
        var errorModel = messageContainer.getAllMessagesWithLogin(badLogin)
                .assertThat()
                .statusCode(404)
                .extract()
                .body()
                .as(ErrorModel.class);
        assertThat("Wrong error message",
                errorModel.getMessage(), containsString(badLogin + " not found"));

    }

    @Test
    void getAllMessagesCorrectWithDates() {
        MessageDTOResponse message = messageContainer.addMessage(user.getLogin(), messageDTORequest)
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .as(MessageDTOResponse.class);
        var result = messageContainer.getAllMessages(
                        Instant.from(ZonedDateTime.now().minusDays(10)).toString(),
                        Instant.now().toString()
                )
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("", MessageDTOResponse.class);
        assertThat("Wrong data returned",
                result, equalTo(List.of(message)));

    }

    @Test
    void getAllMessagesCorrectWithEndDate() {
        MessageDTOResponse message = messageContainer.addMessage(user.getLogin(), messageDTORequest)
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .as(MessageDTOResponse.class);
        var result = messageContainer.getAllMessages(
                        null,
                        Instant.now().toString()
                )
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("", MessageDTOResponse.class);
        assertThat("Wrong data returned",
                result, equalTo(List.of(message)));

    }

    @Test
    void getAllMessagesCorrectWithStartDate() {
        MessageDTOResponse message = messageContainer.addMessage(user.getLogin(), messageDTORequest)
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .as(MessageDTOResponse.class);
        var result = messageContainer.getAllMessages(
                        Instant.from(ZonedDateTime.now().minusDays(10)).toString(),
                        null
                )
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("", MessageDTOResponse.class);
        assertThat("Wrong data returned",
                result, equalTo(List.of(message)));

    }

    @Test
    void getAllMessagesCorrectWithOutDates() {
        MessageDTOResponse message = messageContainer.addMessage(user.getLogin(), messageDTORequest)
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .as(MessageDTOResponse.class);
        var result = messageContainer.getAllMessages(
                        null,
                        null
                )
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("", MessageDTOResponse.class);
        assertThat("Wrong data returned",
                result, equalTo(List.of(message)));

    }

    @Test
    void getAllMessagesCorrectWithEmptyResult() {
        MessageDTOResponse message = messageContainer.addMessage(user.getLogin(), messageDTORequest)
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .as(MessageDTOResponse.class);
        var result = messageContainer.getAllMessages(
                        Instant.from(ZonedDateTime.now().minusDays(10)).toString(),
                        Instant.from(ZonedDateTime.now().minusDays(5)).toString()
                )
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("", MessageDTOResponse.class);
        assertThat("Wrong data returned",
                result, equalTo(new ArrayList<>()));

    }

}