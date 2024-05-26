package ru.egorov.effectiveexample.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import ru.egorov.effectiveexample.validator.UserEmailExist;

@Getter
@Setter
@Schema(description = "Email dto.")
public class EmailDto {

    @UserEmailExist(message = "Пользователь с даной электронной почтой зарегистрирован.")
    @NotEmpty(message = "Укажите адрес электронной почты!")
    @Email(message = "Укажите корректный адрес электронной почты!")
    private String email;
}
