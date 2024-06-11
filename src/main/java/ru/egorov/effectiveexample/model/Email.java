package ru.egorov.effectiveexample.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "emails")
@Schema(description = "Entity for email user.")
public class Email {

    public Email(String email, Boolean isMain, String userId) {
        this.email = email;
        this.isMain = isMain;
        this.userId = userId;
    }

    @Id
    @Schema(description = "id for entity. Generated IDENTITY.")
    private Long id;

    @Column
    @Schema(description = "Email", example = "test@mail.ru")
    private String email;

    @Schema(description = "The index is the main mail or not", example = "true")
    private Boolean isMain;

    @Column("user_id")
    private String userId;

}
