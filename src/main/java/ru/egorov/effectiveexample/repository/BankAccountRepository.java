package ru.egorov.effectiveexample.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.egorov.effectiveexample.model.BankAccount;

public interface BankAccountRepository extends ReactiveCrudRepository<BankAccount, String> {
}
