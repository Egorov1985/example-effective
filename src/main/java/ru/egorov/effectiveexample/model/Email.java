package ru.egorov.effectiveexample.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "emails")
@Schema(description = "Entity for email user.")
@Getter
@Setter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email1 = (Email) o;
        return Objects.equals(id, email1.id) && Objects.equals(email, email1.email) && Objects.equals(isMain, email1.isMain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, isMain);
    }
}
