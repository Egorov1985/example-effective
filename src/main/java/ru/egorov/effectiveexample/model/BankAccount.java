package ru.egorov.effectiveexample.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(id, that.id) && Objects.equals(deposit, that.deposit) && Objects.equals(startBalance, that.startBalance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deposit, startBalance);
    }
}
