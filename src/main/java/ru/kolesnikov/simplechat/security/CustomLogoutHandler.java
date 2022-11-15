package ru.kolesnikov.simplechat.security;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;
import ru.kolesnikov.simplechat.exceptions.NotAuthorizedException;
import ru.kolesnikov.simplechat.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.kolesnikov.simplechat.security.AuthenticationFilter.SECRET;

@Service
@Slf4j
public class CustomLogoutHandler implements LogoutHandler, LogoutSuccessHandler {

    //    private final UserCache userCache;
    private final AuthService service;

    public CustomLogoutHandler(AuthService service) {
        this.service = service;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {


    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< start logout");
        String login = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(request.getHeader("Authorization").replace("Bearer ", ""))
                .getBody()
                .getSubject();
        log.info(login);
        if (!service.checkAccess(login)) {
            throw new NotAuthorizedException();
        }
        service.logout(login);
        request.removeAttribute("Authorization");
        request.logout();
    }
}
