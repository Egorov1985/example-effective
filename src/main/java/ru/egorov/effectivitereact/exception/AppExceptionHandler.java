package ru.egorov.effectivitereact.exception;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import javax.naming.AuthenticationException;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handlerUserException(UserNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({WebExchangeBindException.class})
    public ResponseEntity<List<String>> handleExceptionUserReg(WebExchangeBindException e) {
        var errors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return ResponseEntity.badRequest().body(errors);
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

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<List<String>> handleExceptionValid(ConstraintViolationException e) {
        List<String> errors = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(value = {ResourceException.class})
    public ResponseEntity<String> handleResourceException(ResourceException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value = {UserAlreadyExistsException.class})
    public ResponseEntity<String> handleResourceConstraintViolation(UserAlreadyExistsException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }


    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleParseDateException(DateTimeParseException e) {
        return ResponseEntity.badRequest().body("Дата должна иметь формат 'ДД.ММ.ГГГГ'.");
    }


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }


}
