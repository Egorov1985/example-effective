package ru.egorov.effectiveexample.repository;

import org.springframework.data.repository.CrudRepository;
import ru.egorov.effectiveexample.model.Token;

import java.math.BigDecimal;
import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token, BigDecimal> {
    Optional<Token> findByToken(String token);
}
