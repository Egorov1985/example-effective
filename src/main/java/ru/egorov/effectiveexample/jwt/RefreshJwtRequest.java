package ru.egorov.effectiveexample.jwt;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshJwtRequest {

    @NotNull(message = "Токен пуст.")
    public String refreshToken;

}