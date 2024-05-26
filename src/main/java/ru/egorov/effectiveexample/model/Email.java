package ru.egorov.effectiveexample.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "emails")
@Schema(description = "Entity for email user.")
public class Email implements Serializable {

    public Email(String email, Boolean isMain) {
        this.email = email;
        this.isMain = isMain;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "id for entity. Generated IDENTITY.")
    private Long id;
    @Column(unique = true)
    @Schema(description = "Email", example = "test@mail.ru")
    private String email;
    @Schema(description = "The index is the main mail or not", example = "true")
    private Boolean isMain;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    @Schema(description = "User of email", implementation = User.class)
    private User user;
}
