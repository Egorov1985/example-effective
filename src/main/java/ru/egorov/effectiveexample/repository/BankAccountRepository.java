package ru.egorov.effectiveexample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.egorov.effectiveexample.model.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {

    @Modifying
    @Transactional
    @Query("UPDATE BankAccount b set b.deposit=" +
            "(b.deposit+b.deposit*5/100)  where (b.deposit+b.deposit*5/100) < (b.startBalance+b.startBalance*207/100)")
    void incrementDepositUser();

}
