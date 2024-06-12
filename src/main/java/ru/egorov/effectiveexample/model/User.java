package ru.egorov.effectiveexample.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.validation.annotation.Validated;
import ru.egorov.effectiveexample.validator.UserLoginExist;

import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "users")
@Schema(description = "Entity for user.")
@Validated
public class User {
    @Id
    private String id;

    @JsonIgnore
    private String firstName;

    @JsonIgnore
    private String middleName;

    @JsonIgnore
    private String lastName;

    @NotBlank(message = "Введите ваш логин!")
    @UserLoginExist
    private String login;

    @JsonIgnore
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;

    @JsonIgnore
    private LocalDate birthday;


}
