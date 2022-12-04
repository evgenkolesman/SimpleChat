package ru.kolesnikov.simplechat.exceptionhandler;

import com.devskiller.friendly_id.FriendlyId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import ru.kolesnikov.simplechat.exceptions.*;
import ru.kolesnikov.simplechat.model.ErrorModel;

import java.util.Optional;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler {
    private final Environment environment;

    public RestResponseEntityExceptionHandler(Environment environment) {
        this.environment = environment;
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorModel> handleException(UserNotFoundException exception) {
        var errorId = FriendlyId.createFriendlyId();
        log.info(String.format("%s %s", errorId, exception.getMessage()));
        return new ResponseEntity<>(new ErrorModel(errorId,
                exception.getMessage(),
                HttpStatus.NOT_FOUND),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorModel> handleException(MessageNotFoundException exception) {
        var errorId = FriendlyId.createFriendlyId();
        log.info(String.format("%s %s", errorId, exception.getMessage()));
        return new ResponseEntity<>(new ErrorModel(errorId,
                exception.getMessage(),
                HttpStatus.NOT_FOUND),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorModel> handleException(BadLoginMessageException exception) {
        var errorId = FriendlyId.createFriendlyId();
        log.info(String.format("%s %s", errorId, exception.getMessage()));
        return new ResponseEntity<>(new ErrorModel(errorId,
                exception.getMessage(),
                HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorModel> handleException(UserWasRegisteredException exception) {
        var errorId = FriendlyId.createFriendlyId();
        log.info(String.format("%s %s", errorId, exception.getMessage()));
        return new ResponseEntity<>(new ErrorModel(errorId,
                exception.getMessage(),
                HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorModel> handleException(MethodArgumentNotValidException exception) {
        var errorId = FriendlyId.createFriendlyId();
        log.info(String.format("%s %s", errorId, exception.getMessage()));
        return new ResponseEntity<>(new ErrorModel(errorId,
                Optional.ofNullable(exception.getFieldError()).orElseThrow().getDefaultMessage(),
                HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorModel> handleException(NotAuthorizedException exception) {
        var errorId = FriendlyId.createFriendlyId();
        var errorMessage = environment.getProperty("exceptions.notEnoughPermissions");
        log.info(String.format("%s %s", errorId, exception.getMessage()));
        return new ResponseEntity<>(new ErrorModel(errorId,
                errorMessage,
                HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS),
                HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }


}
