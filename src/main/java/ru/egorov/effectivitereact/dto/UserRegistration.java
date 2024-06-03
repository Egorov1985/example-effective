package ru.egorov.effectivitereact.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.egorov.effectivitereact.validator.UserEmailExist;
import ru.egorov.effectivitereact.validator.UserLoginExist;
import ru.egorov.effectivitereact.validator.UserPhoneExist;

import java.io.Serializable;


@Data
@AllArgsConstructor
@Schema(description = "Dto for registration new User.")
public class UserRegistration implements Serializable {


    @UserLoginExist(message = "Пользователь с данным логином зарегистрирован.")
    @NotNull(message = "Укажите ваш логин для регистрации!")
    @Schema(description = "login", example = "Login")
    private String login;

    @NotNull(message = "Пароль не может быть пустым!")
    @Schema(description = "password", example = "pass")
    private String password;

    @NotNull(message = "Укажите ваш депозит!")
    @Schema(description = "Start deposit", example = "1000.00")
    private Double deposit;

    @UserPhoneExist(message = "Телефон занят другим пользователем!")
    @NotNull(message = "Укажите номер телефона!")
    @Pattern(regexp = "\\d+", message = "Неверный формат номера телефона. Номер телефона должен быть без '+7', 8, скобок и дефисов.")
    @Schema(description = "Phone number", example = "9298887766")
    private String phone;

    @UserEmailExist(message = "Пользователь с данным почтовым адресом зарегистрирован.")
    @NotNull(message = "Укажите адрес электронной почты!")
    @Schema(description = "Email", example = "test@mail.ru")
    private String email;


}
