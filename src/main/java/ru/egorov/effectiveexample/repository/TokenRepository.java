package ru.egorov.effectiveexample.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.math.BigInteger;

public interface TokenRepository extends ReactiveCrudRepository<TokenRepository, BigInteger> {
}
