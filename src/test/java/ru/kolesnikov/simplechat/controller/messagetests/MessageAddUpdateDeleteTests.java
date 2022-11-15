package ru.kolesnikov.simplechat.controller.messagetests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import ru.kolesnikov.simplechat.controller.TestAbstractIntegration;
import ru.kolesnikov.simplechat.controller.containermethods.ContainerAuthTestMethods;
import ru.kolesnikov.simplechat.controller.containermethods.ContainerMessageTestMethods;
import ru.kolesnikov.simplechat.controller.containermethods.ContainerUserTestMethods;
import ru.kolesnikov.simplechat.controller.containermethods.dto.TestUserDTOAuth;
import ru.kolesnikov.simplechat.controller.containermethods.dto.TestUserDTORegistration;
import ru.kolesnikov.simplechat.controller.dto.MessageDTORequest;
import ru.kolesnikov.simplechat.controller.dto.MessageDTOResponse;
import ru.kolesnikov.simplechat.controller.dto.UserDTOResponse;
import ru.kolesnikov.simplechat.model.ErrorModel;
import ru.kolesnikov.simplechat.repository.AuthRepository;
import ru.kolesnikov.simplechat.repository.MessageRepository;
import ru.kolesnikov.simplechat.repository.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class MessageAddUpdateDeleteTests extends TestAbstractIntegration {

    @Autowired
    private ContainerAuthTestMethods authTestMethods;

    @Autowired
    private ContainerMessageTestMethods messageContainer;

    @Autowired
    private ContainerUserTestMethods userContainer;

    @Autowired
    private ContainerAuthTestMethods containerAuthTestMethods;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private Environment environment;

    @LocalServerPort
    private int port;
    private TestUserDTORegistration userRegistration;
    private UserDTOResponse user;
    private MessageDTORequest messageDTORequest;
    private String token;


    @BeforeEach
    void testDataProduce() {
        RestAssured.port = port;

        var name = "name";
        var login = "login";
        var photoPath = "photoPath";
        String password = "password";

        userRegistration = new TestUserDTORegistration(login,
                name,
                "surname",
                password
                , photoPath
        );
        messageDTORequest = new MessageDTORequest("Message to chat");

        user = userContainer.addUser(userRegistration).assertThat().statusCode(200)
                .extract().as(UserDTOResponse.class);
        token = authTestMethods.checkAuthAndReturnToken(new TestUserDTOAuth(user.getLogin(), password));

    }

    @AfterEach
    void testDataClear() {
        authRepository.deleteAll();
        messageRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void addMessageCorrectTest() {
        String token =
                containerAuthTestMethods.checkAuthAndReturnToken(new TestUserDTOAuth(user.getLogin(),
                        userRegistration.getPassword()));
        MessageDTOResponse message = messageContainer.addMessage(user.getLogin(), messageDTORequest, token)
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .as(MessageDTOResponse.class);
        assertThat("Wrong message was returned",
                message.getMessageBody(),
                equalTo(messageDTORequest.messageBody()));
    }

    @Test
    void addMessageDoubleCorrectTest() {
        MessageDTOResponse message = messageContainer.addMessage(user.getLogin(), messageDTORequest, token)
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .as(MessageDTOResponse.class);
        messageContainer.addMessage(user.getLogin(), messageDTORequest, token)
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .as(MessageDTOResponse.class);
    }

    @Test
    void addMessageBadLoginTest() {
        var errorModel = messageContainer.addMessage(user.getName(), messageDTORequest, token)
                .assertThat()
                .statusCode(451)
                .extract()
                .body()
                .as(ErrorModel.class);
        assertThat("Bad data returned",
                errorModel.getMessage(),
                equalTo(environment.getProperty("exceptions.notEnoughPermissions")));


    }

    @Test
    void updateMessageCorrectTest() {
        MessageDTOResponse message = messageContainer.addMessage(user.getLogin(), messageDTORequest, token)
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .as(MessageDTOResponse.class);
        String new_body = "New Body";
        MessageDTOResponse messageUpdate = messageContainer.updateMessage(message.getLogin(),
                        message.getId(),
                        new MessageDTORequest(new_body),
                        token)
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .as(MessageDTOResponse.class);
        assertThat("Message not correctly updated",
                messageUpdate.getMessageBody(),
                equalTo(new_body));
    }

    @Test
    void updateMessageNotCorrectIdTest() {
        String new_body = "New Body";
        String badId = "sdasd";
        var errorModel = messageContainer.updateMessage("login",
                        badId,
                        new MessageDTORequest(new_body),
                        token)
                .assertThat()
                .statusCode(404)
                .extract()
                .body()
                .as(ErrorModel.class);
        assertThat("Message not correctly updated",
                errorModel.getMessage().split(" ", 2)[1],
                equalTo(String.format("Problems with message ID: %s not found", badId)));
    }

    @Test
    void updateMessageNotCorrectLoginTest() {
        String new_body = "New Body";
        String badId = "sdasd";
        String badLogin = "login2";
        var errorModel = messageContainer.updateMessage(badLogin,
                        badId,
                        new MessageDTORequest(new_body),
                        token)
                .assertThat()
                .statusCode(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.value())
                .extract()
                .body()
                .as(ErrorModel.class);
        assertThat("Bad data returned", errorModel.getMessage(),
                equalTo(environment.getProperty("exceptions.notEnoughPermissions")));
    }

    @Test
    void deleteMessageCorrectTest() {
        MessageDTOResponse message = messageContainer.addMessage(user.getLogin(), messageDTORequest, token)
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .as(MessageDTOResponse.class);
        messageContainer.deleteMessage(message.getLogin(),
                        message.getId(),
                        token)
                .assertThat()
                .statusCode(204);
    }

    @Test
    void deleteMessageNotCorrectTest() {
        String badLogin = "idsdasd";
        var errorModel = messageContainer.deleteMessage("login",
                        badLogin,
                        token)
                .assertThat()
                .statusCode(404)
                .extract()
                .body()
                .as(ErrorModel.class);
        assertThat("Message not correctly updated",
                errorModel.getMessage().split(" ", 2)[1],
                equalTo(String.format("Problems with message ID: %s not found", badLogin)));
    }

}
