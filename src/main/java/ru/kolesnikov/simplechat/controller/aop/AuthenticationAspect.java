package ru.kolesnikov.simplechat.controller.aop;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ru.kolesnikov.simplechat.service.AuthService;

import javax.servlet.http.HttpServletRequest;

import static ru.kolesnikov.simplechat.security.AuthenticationFilter.SECRET;

@Component
@Aspect
@RequiredArgsConstructor
public class AuthenticationAspect {

    private final AuthService service;

    @Before("execution(* getUserByLogin(String))")
    public void beforeGetUserByLoginAdvice(JoinPoint joinPoint) {
        String method = "getUserByLogin";
        randomAuthorization(joinPoint, method);
    }


    @Before("execution(* getUserAllUsers(String))")
    public void beforeGetAllUsersAdvice(JoinPoint joinPoint) {
        String method = "getUserAllUsers";
        randomAuthorization(joinPoint, method);
    }

    @Before("execution(* " +
            "ru.kolesnikov.simplechat.controller.UserController.updateUser(String, ru.kolesnikov.simplechat.controller.dto.UserDTORegistration))")
    public void beforeUpdateUserAdvice(JoinPoint joinPoint) {
        String method = "UserDTORegistration";
        randomAuthorization(joinPoint, method);
    }

    @Before("execution(void ru.kolesnikov.simplechat.controller.UserController.deleteUser(String))")
    public void beforeDeleteUserAdvice(JoinPoint joinPoint) {
        String method = "deleteUser";
        randomAuthorization(joinPoint, method);
    }

    @Before("execution(* ru.kolesnikov.simplechat.controller.MessageController.addMessage(String, ru.kolesnikov.simplechat.controller.dto.MessageDTORequest))")
    public void beforeAddMessageAdvice(JoinPoint joinPoint) {
        String method = "addMessage";
        randomAuthorization(joinPoint, method);
    }

    @Before("execution(* ru.kolesnikov.simplechat.controller.MessageController.updateMessage(" +
            "String, String, ru.kolesnikov.simplechat.controller.dto.MessageDTORequest))")
    public void beforeUpdateMessageAdvice(JoinPoint joinPoint) {
        String method = "updateMessage";
        randomAuthorization(joinPoint, method);
    }

    @Before("execution(* ru.kolesnikov.simplechat.controller.MessageController.getAllMessagesWithLogin(String))")
    public void beforeGetAllMessagesWithLoginAdvice(JoinPoint joinPoint) {
        String method = "getAllMessagesWithLogin";
        randomAuthorization(joinPoint, method);
    }

    @Before("execution(* ru.kolesnikov.simplechat.controller.MessageController.getMessageById(String, String))")
    public void beforeGetMessageByIdAdvice(JoinPoint joinPoint) {
        String method = "getMessageById";
        randomAuthorization(joinPoint, method);
    }

    @Before("execution(* ru.kolesnikov.simplechat.controller.MessageController.deleteMessage(String))")
    public void beforeDeleteMessageAdvice(JoinPoint joinPoint) {
        String method = "deleteMessage";
        randomAuthorization(joinPoint, method);

    }

    @Before("execution(* ru.kolesnikov.simplechat.controller.AuthUserController.getAllActiveUsers(String))")
    public void beforeGetAllActiveUsersAdvice(JoinPoint joinPoint) {
        String method = "getAllActiveUsers";
        randomAuthorization(joinPoint, method);
    }

    @Before("execution(* ru.kolesnikov.simplechat.security.CustomLogoutHandler.onLogoutSuccess(" +
            "javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication )))")
    public void beforeOnLogoutSuccessAdvice(JoinPoint joinPoint) {

        var request = (HttpServletRequest) joinPoint.getArgs()[0];

        String login = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(request.getHeader("Authorization").replace("Bearer ", ""))
                .getBody()
                .getSubject();
        System.out.printf("<<<<<<<<<<<<<<<<<<<< aspect onLogoutSuccess %s >>>>>>>>>>>>>>>>> %s%n",
                joinPoint.getSignature().toString(), login);
        service.authorize(login);
    }

    private void randomAuthorization(JoinPoint joinPoint, String delete) {
        var login = joinPoint.getArgs()[0].toString();
        System.out.printf("<<<<<<<<<<<<<<<<<<<< aspect " + delete + " %s >>>>>>>>>>>>>>>>> %s%n",
                joinPoint.getSignature().toString(), login);
        service.authorize(login);
    }
}
