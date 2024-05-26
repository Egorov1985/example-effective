package ru.egorov.effectiveexample.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "users")
@Schema(description = "Entity for user.")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "id for entity. Generated UUID random." )
    private String id;

    @JsonIgnore
    @Schema(description = "User first name", example ="Александр")
    private String firstName;

    @JsonIgnore
    @Schema(description = "User middle name", example ="Николавеич")
    private String middleName;

    @JsonIgnore
    @Schema(description = "User last name", example ="Егоров")
    private String lastName;

    @NotBlank(message = "Введите ваш логин!")
    @Schema(description = "login", example = "Login")
    private String login;

    @JsonIgnore
    @NotBlank(message = "Пароль не может быть пустым")
    @Schema(description = "password", example = "pass")
    private String password;

    @JsonIgnore
    @Schema(description = "Birthday", example ="12.02.1987")
    private LocalDate birthday;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @Schema(description = "user account of bank", implementation = BankAccount.class)
    private BankAccount bankAccount;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    @JsonIgnore
    @Schema(description = "List phone user", implementation = Phone.class)
    private List<Phone> phones = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    @JsonIgnore
    @Schema(description = "List email user", implementation = Email.class)
    private List<Email> emails = new ArrayList<>();

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
        this.bankAccount.setUser(this);
    }

}
