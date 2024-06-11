package ru.egorov.effectivitereact.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.egorov.effectivitereact.model.BankAccount;

public interface BankAccountRepository extends ReactiveCrudRepository<BankAccount, String> {
}
