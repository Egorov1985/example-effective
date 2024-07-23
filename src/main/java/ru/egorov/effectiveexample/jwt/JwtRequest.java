package ru.egorov.effectiveexample.jwt;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Schema(description = "The body for receiving a new access and refresh token.")
public class JwtRequest {

    @NotNull(message = "Введите логин.")
    @Schema(description = "login user", example = "user1")
    private String login;

    @NotNull(message = "Пароль не может быть пустым!")
    @Schema(description = "password user", example = "pass")
    @Length(min = 4, max = 10, message = "Пароль должен содержать не менее 4 и не более 10 символов.")
    private String password;
}
