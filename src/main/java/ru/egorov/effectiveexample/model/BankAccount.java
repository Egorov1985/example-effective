package ru.egorov.effectiveexample.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Schema(description = "Entity account of bank for user.")
public class BankAccount implements Serializable {

    public BankAccount(Double deposit) {
        this.deposit = deposit;
        this.startBalance = deposit;
    }

    @Id
    @JsonIgnore
    @Schema(description = "id for entity. Generated user.")
    private String id;

    @Column(name = "deposit")
    @PositiveOrZero(message = "Баланс не может быть отрицательным.")
    @NotNull(message ="Депозит не может быть нулевым!")
    @Schema(description = "Start deposit", example = "1000.00")
    private Double deposit;

    @Column(name = "start_balance", nullable = false)
    @JsonIgnore
    @Schema(description = "Start balance for increment start deposit", example = "1000.00")
    private Double startBalance;

    @OneToOne
    @MapsId
    @JsonIgnore
    @Schema(description = "User of account", implementation = User.class)
    private User user;
}
