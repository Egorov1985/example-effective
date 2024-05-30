package ru.egorov.effectiveexample.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.egorov.effectiveexample.validator.UserPhoneExist;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "phones")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Phone implements Serializable {

    public Phone(String number, Boolean isMain) {
        this.number = number;
        this.isMain = isMain;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "id for entity. Generated IDENTITY.")
    private Long id;

    @Column(unique = true)
    @Schema(description = "Phone number", example = "9298887766")
    private String number;
    @Schema(description = "The index is the main phone or not", example = "true")
    private Boolean isMain;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    @Schema(description = "User of phone", implementation = User.class)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return Objects.equals(id, phone.id) && Objects.equals(number, phone.number) && Objects.equals(isMain, phone.isMain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, isMain);
    }
}
