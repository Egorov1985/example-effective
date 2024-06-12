package ru.egorov.effectiveexample.model;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigInteger;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Token {
    @Id
    private BigInteger id;
    private String login;
    private String token;
    boolean isActive;
}
