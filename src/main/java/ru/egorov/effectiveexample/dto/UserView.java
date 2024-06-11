package ru.egorov.effectivitereact.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.egorov.effectivitereact.model.BankAccount;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "User view for controller")
public class UserView {
    @Schema(description = "login", example = "Login")
    private String login;

    private BankAccount bankAccount;
    @Schema(description = "User first name", example = "Александр")
    private String firstName;
    @Schema(description = "User middle name", example = "Николавеич")
    private String middleName;
    @Schema(description = "User last name", example = "Егоров")
    private String lastName;
    @Schema(description = "Birthday", example = "12.02.1987")
    private String birthday;
}
