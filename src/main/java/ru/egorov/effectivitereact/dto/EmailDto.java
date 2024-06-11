package ru.egorov.effectivitereact.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.egorov.effectivitereact.validator.UserEmailExist;


@Getter
@Setter
@Schema(description = "Email dto.")
public class EmailDto {

   // @UserEmailExist(message = "Почта уже добавлена или занята другим пользователем!")
    @NotNull(message = "Укажите адрес электронной почты!")
    @Email(message = "Укажите корректный адрес электронной почты!")
    private String email;
}
