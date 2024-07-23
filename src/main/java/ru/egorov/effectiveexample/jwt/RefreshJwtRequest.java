package ru.egorov.effectiveexample.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "The body for receiving a new refresh token or new access token.")
public class RefreshJwtRequest {

    @NotNull(message = "Токен пуст.")
    @Schema(description = "Old refresh token", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTcyMzAzNzg1M30.RmJYg6LWvBuMI4FTkPOQmpUzm4M3mjVsOMUV2it4_1P4GQp-C-TsPdysdLbhyA3euY__DUVzuEsun6eERFkwFQ")
    public String refreshToken;
}