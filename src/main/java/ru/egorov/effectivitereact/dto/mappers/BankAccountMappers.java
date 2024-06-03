package ru.egorov.effectivitereact.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Mono;
import ru.egorov.effectivitereact.model.BankAccount;

@Mapper
public interface BankAccountMappers {

    BankAccountMappers INSTANCE = Mappers.getMapper(BankAccountMappers.class);

    default BankAccount monoToEntity(Mono<BankAccount> mono) {
        BankAccount bankAccount = new BankAccount();
        mono.subscribe(b -> {
            bankAccount.setDeposit(b.getDeposit());
            bankAccount.setStartBalance(b.getStartBalance());
            bankAccount.setUserId(b.getUserId());
        });
        return bankAccount;
    }
}
