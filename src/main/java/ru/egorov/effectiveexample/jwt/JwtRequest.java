package ru.egorov.effectiveexample.jwt;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class JwtRequest {

    @NotNull(message = "Введите логин.")
    private String login;
    @NotNull(message = "Пароль не может быть пустым!")
    @Length(min = 4, max = 10, message = "Пароль должен содержать не менее 4 и не более 10 символов.")
    private String password;
}
