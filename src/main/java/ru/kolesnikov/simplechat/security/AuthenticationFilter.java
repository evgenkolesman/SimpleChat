package ru.kolesnikov.simplechat.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.kolesnikov.simplechat.model.LoginRequestModel;
import ru.kolesnikov.simplechat.service.AuthService;
import ru.kolesnikov.simplechat.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static ru.kolesnikov.simplechat.kafka.KafkaTopicConfig.KAFKA_TOPIC;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String SECRET = "DSADDSAFewqrwrfvz";
    private final UserService userService;
    private final AuthService authService;
    private final KafkaTemplate kafkaTemplate;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                UserService userService,
                                AuthService authService,
                                KafkaTemplate kafkaTemplate) {
        this.userService = userService;
        this.authService = authService;
        this.kafkaTemplate = kafkaTemplate;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginRequestModel credentials = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequestModel.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getLogin(),
                            credentials.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (IOException e) {
            logger.info("Bad credentials");
            throw new RuntimeException(e);

        }
//        finally {
//            logger.info("-----AuthFilter part---- with token " + authentication.toString());
//        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String userName = ((User) authResult.getPrincipal()).getUsername();


        boolean checkAuthorization = userService.checkAuthorization(userName);

        if (checkAuthorization) {
            authService.authorize(userName);
            kafkaTemplate.send(KAFKA_TOPIC,
                    "AUTHORIZATION_TOKEN");
        } else {
            log.info("NOT AUTHORIZED");
        }

        ru.kolesnikov.simplechat.model.User user = userService.findUserByLogin(userName);

        String token = Jwts.builder()
                .setSubject(user.getLogin())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(String.valueOf(10000000L))))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();

        response.addHeader("Token", token);
        logger.info("=========== token is " + token);
        response.addHeader("Login", user.getLogin());
        logger.info("=========== userId is " + user.getLogin());
    }
}
