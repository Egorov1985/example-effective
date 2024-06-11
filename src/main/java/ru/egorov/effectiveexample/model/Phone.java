package ru.egorov.effectiveexample.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Table(name = "phones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Phone  {


    public Phone(String number, Boolean isMain, String userId) {
        this.number = number;
        this.isMain = isMain;
        this.userId = userId;
    }

    @Id
    @Schema(description = "id for entity. Generated IDENTITY.")
    private Long id;

    @Schema(description = "Phone number", example = "9298887766")
    private String number;

    @Schema(description = "The index is the main phone or not", example = "true")
    private Boolean isMain;

    @Column("user_id")
    private String userId;

}
