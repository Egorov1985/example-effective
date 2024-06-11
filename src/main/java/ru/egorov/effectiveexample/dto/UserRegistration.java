package ru.egorov.effectiveexample.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.egorov.effectiveexample.validator.UserEmailExist;
import ru.egorov.effectiveexample.validator.UserLoginExist;
import ru.egorov.effectiveexample.validator.UserPhoneExist;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dto for registration new User.")
public class UserRegistration {


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
    @Email(message = "Введите корректный почтовый адрес!")
    @NotBlank(message = "Укажите адрес электронной почты!")
    @Schema(description = "Email", example = "test@mail.ru")
    private String email;


}
