package ru.egorov.effectiveexample.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.egorov.effectiveexample.validator.UserPhoneExist;

import java.io.Serializable;

@Entity
@Table(name = "phones")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

}
