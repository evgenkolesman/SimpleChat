package ru.kolesnikov.simplechat.security;

import com.devskiller.friendly_id.FriendlyId;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ru.kolesnikov.simplechat.model.ErrorModel;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static ru.kolesnikov.simplechat.security.AuthenticationFilter.SECRET;

/**
 * FOR AUTHORIZATION (REGISTRY)
 */
@Slf4j
public class AuthorizationFilter extends BasicAuthenticationFilter {
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer";

    public AuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String authHeader = request.getHeader(AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            chain.doFilter(request, response);
            return;
        }
        try {
            UsernamePasswordAuthenticationToken token = getAuthentication(request, response);
            SecurityContextHolder.getContext().setAuthentication(token);

        } catch (MalformedJwtException exception) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            String wrong_token = new ObjectMapper(new MappingJsonFactory()).writeValueAsString(new ErrorModel(
                    FriendlyId.createFriendlyId(),
                    "Wrong token",
                    HttpStatus.FORBIDDEN
            ));
            response.getWriter().write(wrong_token);
            return;
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request,
                                                                  HttpServletResponse response) {
        String authHeader = request.getHeader(AUTHORIZATION);

        if (authHeader == null) {
            return null;
        }

        String token = authHeader.replace(BEARER, "").trim();
        String login = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return new UsernamePasswordAuthenticationToken(login, null, new ArrayList<>());

    }
}
