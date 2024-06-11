package ru.egorov.effectivitereact.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@Schema(description = "Dto for add information for user.")

public class UserDtoInfo {

    @NotNull(message = "{message.firstName}")
    @Schema(description = "User first name", example ="Александр")
    private String firstName;

    @NotNull(message = "{message.middleName}")
    @Schema(description = "User middle name", example ="Николавеич")
    private String middleName;

    @NotNull(message = "{message.lastName}")
    @Schema(description = "User last name", example ="Егоров")
    private String lastName;

    @NotNull(message = "{message.birthday}")
    @Schema(description = "Birthday", example ="12.02.1987")
    private String birthday;

}
