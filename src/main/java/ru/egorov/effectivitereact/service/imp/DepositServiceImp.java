package ru.egorov.effectivitereact.service.imp;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.egorov.effectivitereact.mappers.BankAccountMappers;
import ru.egorov.effectivitereact.exception.ResourceException;
import ru.egorov.effectivitereact.exception.UserNotFoundException;
import ru.egorov.effectivitereact.model.BankAccount;
import ru.egorov.effectivitereact.model.User;
import ru.egorov.effectivitereact.repository.BankAccountRepository;
import ru.egorov.effectivitereact.repository.PhonesRepository;
import ru.egorov.effectivitereact.repository.UsersRepository;
import ru.egorov.effectivitereact.service.DepositService;


@Service
@RequiredArgsConstructor
public class DepositServiceImp implements DepositService {
    private final BankAccountRepository bankAccountRepository;
    private final UsersRepository userRepository;
    private final PhonesRepository phoneRepository;
    private final Logger logger = LoggerFactory.getLogger(DepositServiceImp.class);

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void incrementDeposit() {
        //  bankAccountRepository.incrementDepositUser();
        logger.info("Increment balance for user!");
    }

    @Override
    @Transactional
    public Mono<Boolean> transferMoneyToUser(String login, String numberPhone, Double depositTransfer) {

        Mono<User> userFrom = userRepository.findUsersByLogin(login)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found!")));

        Mono<BankAccount> accountMonoFrom = bankAccountRepository.findById(userFrom.map(User::getId))
                .switchIfEmpty(Mono.error(new ResourceException("Проверьте ваш счёт!")));

        Mono<String> idUserTo = phoneRepository.getUserIdFrom(numberPhone)
                .switchIfEmpty(Mono.error(new UserNotFoundException("Пользователь с таким номером не зарегистрирован.")));

        Mono<BankAccount> accountMonoTo = bankAccountRepository.findById(idUserTo)
                .switchIfEmpty(Mono.error(new ResourceException("У пользователя нет счета в банке!")));

        if (!accountMonoTo.equals(accountMonoFrom)) {
            BankAccount bankAccountFrom = BankAccountMappers.INSTANCE.monoToEntity(accountMonoFrom);
            BankAccount bankAccountTo = BankAccountMappers.INSTANCE.monoToEntity(accountMonoTo);

            if (depositTransfer <= bankAccountFrom.getDeposit() && depositTransfer != 0) {
                bankAccountFrom.setDeposit(bankAccountFrom.getDeposit() - depositTransfer);
                bankAccountTo.setDeposit(bankAccountTo.getDeposit() + depositTransfer);

                bankAccountRepository.save(bankAccountFrom).doOnNext(
                        res -> logger.info("С депозита пользователя {} снято {} руб." , login, depositTransfer)
                );
                bankAccountRepository.save(bankAccountTo).doOnNext(
                        res -> logger.info("На депозит пользователя {} зачислено {} руб." , numberPhone, depositTransfer)
                );
                logger.info("Пользователь {} успешно перевел пользователю номером телефона {} денежные средтсва в размере {}руб.",
                        login, numberPhone, depositTransfer);
                return Mono.fromSupplier(() -> true);
            } else {
                logger.info("Нельзя перевести больше чем на счету!");
                throw new ResourceException("Нельзя перевести больше чем на счету!");
            }
        } else {
            throw new ResourceException("Нельзя переводить самому себе!");
        }
    }
}
