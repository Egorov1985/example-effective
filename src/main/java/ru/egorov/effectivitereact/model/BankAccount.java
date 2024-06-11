package ru.egorov.effectivitereact.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entity account of bank for user.")
@Builder
public class BankAccount  {

    public BankAccount(Double deposit, String userId) {
        this.deposit = deposit;
        this.startBalance = deposit;
        this.userId = userId;
    }

    @Column("user_id")
    private String userId;


    @Column("deposit")
    @PositiveOrZero(message = "Баланс не может быть отрицательным.")
    @NotNull(message ="Депозит не может быть нулевым!")
    @Schema(description = "Start deposit", example = "1000.00")
    private Double deposit;

    @Column("start_balance")
    @JsonIgnore
    @Schema(description = "Start balance for increment start deposit", example = "1000.00")
    private Double startBalance;

}
