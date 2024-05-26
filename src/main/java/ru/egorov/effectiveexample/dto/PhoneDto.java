package ru.egorov.effectiveexample.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import ru.egorov.effectiveexample.validator.UserPhoneExist;

@Getter
@Setter
@Schema(description = "Phone dto.")
public class PhoneDto {

    @UserPhoneExist(message = "Телефон занят другим пользователем!")
    @NotNull(message = "Укажите номер телефона!")
    @Pattern(regexp = "\\d+", message = "Неверный формат номера телефона. Номер телефона должен быть без '+7', 8, скобок и дефисов.")
    @Schema(description = "Phone number", example = "9298887766")
    private String phone;
}
