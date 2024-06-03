package ru.egorov.effectivitereact.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;



@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "users")
@Schema(description = "Entity for user.")
public class User {
    @Id
    @Schema(description = "id for entity. Generated UUID random.")
    private String id;

    @JsonIgnore
    @Schema(description = "User first name", example = "Александр")
    private String firstName;

    @JsonIgnore
    @Schema(description = "User middle name", example = "Николавеич")
    private String middleName;

    @JsonIgnore
    @Schema(description = "User last name", example = "Егоров")
    private String lastName;

    @NotBlank(message = "Введите ваш логин!")
    @Schema(description = "login", example = "Login")
    private String login;

    @JsonIgnore
    @NotBlank(message = "Пароль не может быть пустым")
    @Schema(description = "password", example = "pass")
    private String password;

    /*@JsonIgnore
    @Schema(description = "Birthday", example = "12.02.1987")
    private LocalDate birthday;
    @Schema(description = "user account of bank", implementation = BankAccount.class)
    private BankAccount bankAccount;

    @ToString.Exclude
    @JsonIgnore
    @Schema(description = "List phone user", implementation = Phone.class)
    private List<Phone> phones = new ArrayList<>();*/


}
