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
    public ResponseEntity<String> handlerUserException(UserNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<List<String>> handleException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(value = {ResourceException.class})
    public ResponseEntity<String> handleResourceException(ResourceException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<List<String>> handleResourceConstraintViolation(ConstraintViolationException e)
    {
        List<String> errors = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessageTemplate)
                .toList();
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleParseDateException(DateTimeParseException e){
        return ResponseEntity.badRequest().body("Дата должна иметь формат 'ДД.ММ.ГГГГ'.");
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<String> handleJwtValidException(JwtException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }


}
