package ru.egorov.effectiveexample.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.egorov.effectiveexample.exception.ResourceException;
import ru.egorov.effectiveexample.exception.UserNotFoundException;
import ru.egorov.effectiveexample.model.BankAccount;
import ru.egorov.effectiveexample.model.User;
import ru.egorov.effectiveexample.repository.BankAccountRepository;
import ru.egorov.effectiveexample.repository.PhoneRepository;
import ru.egorov.effectiveexample.repository.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepositServiceServiceImpTest {

    @InjectMocks
    private DepositServiceServiceImp depositService;
    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private PhoneRepository phoneRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private User userFrom;
    @Mock
    private BankAccount bankAccountFrom;
    @Mock
    private BankAccount bankAccountTo;

    private String login;
    private String idUserFrom;
    private String idUserTo;
    private String phoneUserTo;
    private Double depositForTransfer;

    @BeforeEach
    void setup() {
        login = "login";
        idUserFrom = "idUserFrom";
        idUserTo = "idUserTo";
        phoneUserTo = "9279998877";
        depositForTransfer = 5000.00;
    }

    @Test
    void testWhenSuccessTransferMoney() {
        when(userRepository.findUserByLogin(login)).thenReturn(Optional.of(userFrom));
        when(userFrom.getId()).thenReturn(idUserFrom);
        when(bankAccountRepository.findById(idUserFrom)).thenReturn(Optional.of(bankAccountFrom));
        when(bankAccountFrom.getDeposit()).thenReturn(10000.00);
        when(phoneRepository.getIdFromNumberPhone(phoneUserTo)).thenReturn(Optional.of(idUserTo));
        when(bankAccountRepository.findById(idUserTo)).thenReturn(Optional.of(bankAccountTo));
        //добавлено для правильно корректного отображения логов
        when(userFrom.getLogin()).thenReturn(login);
        Assertions.assertTrue(depositService.transferMoneyToUser(login, phoneUserTo, depositForTransfer));
        Mockito.verify(bankAccountFrom).setDeposit(depositForTransfer);
        Mockito.verify(bankAccountTo).setDeposit(depositForTransfer);
        Mockito.verify(bankAccountRepository, times(2)).save(any(BankAccount.class));
        Mockito.verify(userRepository, times(1)).findUserByLogin(anyString());
        Mockito.verify(bankAccountRepository, times(2)).findById(anyString());
    }

    @Test
    public void testThrowExceptionWhenUserFromEqualsUserTo() {
        when(userRepository.findUserByLogin(login)).thenReturn(Optional.of(userFrom));
        when(userFrom.getId()).thenReturn(idUserFrom);
        when(bankAccountRepository.findById(idUserFrom)).thenReturn(Optional.of(bankAccountFrom));
        when(phoneRepository.getIdFromNumberPhone(phoneUserTo)).thenReturn(Optional.of(idUserTo));
        when(bankAccountRepository.findById(idUserTo)).thenReturn(Optional.of(bankAccountFrom));
        Assertions.assertThrows(ResourceException.class, () -> depositService.transferMoneyToUser(login, phoneUserTo, depositForTransfer));
    }

    @Test
    public void testWhenTransferDepositMoreThenUserBalance() {
        when(userRepository.findUserByLogin(login)).thenReturn(Optional.of(userFrom));
        when(userFrom.getId()).thenReturn(idUserFrom);
        when(bankAccountRepository.findById(idUserFrom)).thenReturn(Optional.of(bankAccountFrom));
        when(bankAccountFrom.getDeposit()).thenReturn(3000.00);
        when(phoneRepository.getIdFromNumberPhone(phoneUserTo)).thenReturn(Optional.of(idUserTo));
        when(bankAccountRepository.findById(idUserTo)).thenReturn(Optional.of(bankAccountTo));
        Assertions.assertThrows(ResourceException.class, () -> depositService.transferMoneyToUser(login, phoneUserTo, depositForTransfer));
    }

    @Test
    public void TestWhenNotFoundBankAccountUserFrom(){
        when(userRepository.findUserByLogin(login)).thenReturn(Optional.of(userFrom));
        when(userFrom.getId()).thenReturn(idUserFrom);
        when(bankAccountRepository.findById(idUserFrom)).thenThrow(ResourceException.class);
        Assertions.assertThrows(ResourceException.class, ()-> depositService.transferMoneyToUser(login, phoneUserTo, depositForTransfer));
    }

    @Test
    public void TestWhenNotFoundBankAccountUserTo(){
        when(userRepository.findUserByLogin(login)).thenReturn(Optional.of(userFrom));
        when(userFrom.getId()).thenReturn(idUserFrom);
        when(bankAccountRepository.findById(idUserFrom)).thenReturn(Optional.of(bankAccountFrom));
        when(phoneRepository.getIdFromNumberPhone(phoneUserTo)).thenReturn(Optional.of(idUserTo));
        when(bankAccountRepository.findById(idUserTo)).thenThrow(ResourceException.class);
        Assertions.assertThrows(ResourceException.class, () -> depositService.transferMoneyToUser(login, phoneUserTo, depositForTransfer));
    }

    @Test
    public void TestWhenUserNotFound(){
        when(userRepository.findUserByLogin(login)).thenThrow(UserNotFoundException.class);
        Assertions.assertThrows(UserNotFoundException.class, () -> depositService.transferMoneyToUser(login, phoneUserTo, depositForTransfer));
    }
}
