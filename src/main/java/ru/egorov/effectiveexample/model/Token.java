package ru.egorov.effectiveexample.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "tokens")
@Builder
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private BigInteger bigInteger;
    private String login;
    private String token;
    boolean isActive;
}
