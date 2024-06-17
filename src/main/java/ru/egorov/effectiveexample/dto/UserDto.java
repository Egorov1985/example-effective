package ru.egorov.effectiveexample.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Schema(description = "Dto for add information for user.")
public class UserDto {

    @NotNull(message = "{message.firstName}")
    @Schema(description = "User first name", example ="Александр")
    protected String firstName;

    @NotNull(message = "{message.middleName}")
    @Schema(description = "User middle name", example ="Николавеич")
    protected String middleName;

    @NotNull(message = "{message.lastName}")
    @Schema(description = "User last name", example ="Егоров")
    protected String lastName;

    @NotNull(message = "{message.birthday}")
    @Schema(description = "Birthday", example ="12.02.1987")
    protected String birthday;

}
