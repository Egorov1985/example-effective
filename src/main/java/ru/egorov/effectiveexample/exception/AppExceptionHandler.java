package ru.egorov.effectiveexample.exception;


import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<MessageErrorResponse> handlerUserException(UserNotFoundException e) {
        return new ResponseEntity<>(new MessageErrorResponse(HttpStatus.NOT_FOUND, List.of(e.getMessage())), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<MessageErrorResponse> handleException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return ResponseEntity.badRequest().body(new MessageErrorResponse(HttpStatus.BAD_REQUEST, errors));
    }

    @ExceptionHandler(value = {ResourceException.class})
    public ResponseEntity<MessageErrorResponse> handleResourceException(ResourceException e) {
        return ResponseEntity.badRequest().body(new MessageErrorResponse(HttpStatus.BAD_REQUEST,
                List.of(e.getMessage())));
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<MessageErrorResponse> handleResourceConstraintViolation(ConstraintViolationException e) {
        List<String> errors = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessageTemplate)
                .toList();
        return ResponseEntity.badRequest().body(new MessageErrorResponse(HttpStatus.BAD_REQUEST,
                errors));
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<MessageErrorResponse> handleParseDateException(DateTimeParseException e) {
        return ResponseEntity.badRequest()
                .body(new MessageErrorResponse(HttpStatus.BAD_REQUEST,
                        List.of("Дата должна иметь формат 'ДД.ММ.ГГГГ'.")));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<MessageErrorResponse> handleJwtValidException(JwtException e) {
        return ResponseEntity.badRequest().body(new MessageErrorResponse(
                HttpStatus.BAD_REQUEST, List.of(e.getMessage())));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<MessageErrorResponse> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.badRequest()
                .body(new MessageErrorResponse(HttpStatus.BAD_REQUEST,
                        List.of(e.getMessage())));
    }


}
