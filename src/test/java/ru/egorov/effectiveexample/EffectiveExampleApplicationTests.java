package ru.egorov.effectiveexample;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.egorov.effectiveexample.exception.ResourceException;
import ru.egorov.effectiveexample.model.BankAccount;
import ru.egorov.effectiveexample.model.User;
import ru.egorov.effectiveexample.repository.BankAccountRepository;
import ru.egorov.effectiveexample.repository.PhoneRepository;
import ru.egorov.effectiveexample.repository.UserRepository;
import ru.egorov.effectiveexample.service.DepositServiceServiceImp;

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
class EffectiveExampleApplicationTests {
    @InjectMocks
    private DepositServiceServiceImp depositService;
    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private PhoneRepository phoneRepository;
    @Mock
    private UserRepository userRepository;

    private User user;

    @Mock
    private BankAccount bankAccountFrom;
    @Mock
    private BankAccount bankAccountTo;
    private String phone;
    private Double deposit;
    private String login;
    private String idUserTo;


    @BeforeEach
    public void setup() {
        login = "userFrom";
        idUserTo = "userTo";
        phone = "9251112233";
        user = User.builder().id("id").login("login").bankAccount(bankAccountFrom).build();
        deposit = 2000.00;
    }

    @Test void testWhenUserToEqualsUserFrom(){

        Assertions.assertThrows(ResourceException.class, ()-> bankAccountFrom.equals(bankAccountTo), "Выбросилось не верное исключение!");
    }

    @Test
    public void testWhenNotFoundUserToTransfer(){
        when(phoneRepository.getIdFromNumberPhone(phone)).thenThrow(ResourceException.class);
        Assertions.assertThrows(ResourceException.class, ()-> phoneRepository.getIdFromNumberPhone(phone));
    }

    @Test
    public void testSuccessTransfer() {
        when(userRepository.findUserByLogin(login)).thenReturn(Optional.of(user));
        when(phoneRepository.getIdFromNumberPhone(phone)).thenReturn(Optional.of(idUserTo));
        when(bankAccountRepository.findById(user.getLogin())).thenReturn(Optional.of(bankAccountFrom));
        when(bankAccountRepository.findById(idUserTo)).thenReturn(Optional.of(bankAccountTo));
        when(bankAccountFrom.getDeposit()).thenReturn(3000.00);
        Assertions.assertTrue(bankAccountFrom.getDeposit() > deposit);

    }


}
