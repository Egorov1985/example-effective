package ru.egorov.effectiveexample.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MessageErrorResponse {
    private HttpStatus status;
    private List<String> errors;
}
