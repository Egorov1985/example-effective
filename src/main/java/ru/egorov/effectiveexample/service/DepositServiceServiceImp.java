package ru.egorov.effectiveexample.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egorov.effectiveexample.exception.ResourceException;
import ru.egorov.effectiveexample.exception.UserNotFoundException;
import ru.egorov.effectiveexample.model.BankAccount;
import ru.egorov.effectiveexample.model.User;
import ru.egorov.effectiveexample.repository.BankAccountRepository;
import ru.egorov.effectiveexample.repository.PhoneRepository;
import ru.egorov.effectiveexample.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class DepositServiceServiceImp implements DepositService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;
    private final Logger logger = LoggerFactory.getLogger(DepositServiceServiceImp.class);

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void incrementDeposit() {
        bankAccountRepository.incrementDepositUser();
        logger.info("Increment balance for user!");
    }


    @Transactional
    public boolean transferMoneyToUser(String login, String numberPhone, Double depositTransfer) {
        User userFrom = userRepository.findUserByLogin(login).orElseThrow(()
                -> new UserNotFoundException("User not found!"));
        BankAccount accountFromTransfer = bankAccountRepository.findById(userFrom.getId()).orElseThrow();
        BankAccount accountToTransfer = bankAccountRepository.findById(
                        phoneRepository.getIdFromNumberPhone(numberPhone).orElseThrow(() ->
                                new UserNotFoundException("Пользователь с таким номером не зарегистрирован.")))
                .orElseThrow();
        if (accountToTransfer.equals(accountFromTransfer)) {
            throw new ResourceException("Нельзя переводить самому себе!");
        }

        if (depositTransfer <= accountFromTransfer.getDeposit()) {
            accountFromTransfer.setDeposit(accountFromTransfer.getDeposit() - depositTransfer);
            accountToTransfer.setDeposit(accountToTransfer.getDeposit() + depositTransfer);

            bankAccountRepository.save(accountFromTransfer);
            bankAccountRepository.save(accountToTransfer);
            logger.info("Пользователь {} успешно перевел пользователю номером телефона {} денежные средтсва в размере {}руб.",
                    userFrom.getLogin(), numberPhone, depositTransfer);
            return true;
        } else {
            logger.info("Нельзя первести больше чем на счету!");
            throw new ResourceException("Нельзя первести больше чем на счету!");
        }
    }
}
