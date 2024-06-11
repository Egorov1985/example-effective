package ru.egorov.effectiveexample.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Money dto for transfer!")
public class MoneyTransferDto {
    @NotNull(message = "Введите номер телефона!")
    @Schema(description = "Phone number", example = "9298887766")
    private String phone;
    @NotNull (message = "Введите сумму для трансфера!")
    @Schema(description = "Deposit for transfer", example = "1000.00")
    private Double deposit;
}
