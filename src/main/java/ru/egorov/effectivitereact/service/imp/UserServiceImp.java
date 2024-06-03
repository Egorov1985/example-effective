package ru.egorov.effectivitereact.service.imp;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.egorov.effectivitereact.dto.UserDtoInfo;
import ru.egorov.effectivitereact.dto.UserRegistration;
import ru.egorov.effectivitereact.dto.UserView;
import ru.egorov.effectivitereact.dto.mappers.UserMappers;
import ru.egorov.effectivitereact.exception.ResourceException;
import ru.egorov.effectivitereact.exception.UserAlreadyExistsException;
import ru.egorov.effectivitereact.model.BankAccount;
import ru.egorov.effectivitereact.model.Email;
import ru.egorov.effectivitereact.model.Phone;
import ru.egorov.effectivitereact.model.User;
import ru.egorov.effectivitereact.repository.BankAccountRepository;
import ru.egorov.effectivitereact.repository.EmailsRepository;
import ru.egorov.effectivitereact.repository.PhonesRepository;
import ru.egorov.effectivitereact.repository.UsersRepository;
import ru.egorov.effectivitereact.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UsersRepository userRepository;
    private final PhonesRepository phonesRepository;
    private final EmailsRepository emailRepository;
    private final BankAccountRepository bankRepository;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);

    @Override
    @Transactional
    public Mono<UserView> createUser(UserRegistration userRegistration) {
        User user = new User();
        user.setLogin(userRegistration.getLogin());
        user.setPassword(userRegistration.getPassword());

        return userRepository.existsUserByLogin(userRegistration.getLogin())
                .flatMap(b -> {
                    if (b)
                        return Mono.error(new UserAlreadyExistsException("User exists!"));
                    return Mono.empty();
                })
                .then(phonesRepository.existsPhonesByNumber(userRegistration.getPhone()))
                .flatMap(b -> {
                            if (b)
                                return Mono.error(new ResourceException("Phone exists!"));
                            return Mono.empty();
                        }
                )
                .then(emailRepository.existsEmailsByEmail(userRegistration.getEmail()))
                .flatMap(b -> {
                            if (b)
                                return Mono.error(new ResourceException("Email exists!"));
                            return Mono.empty();
                        }
                )
                .flatMap(o -> Mono.fromSupplier(UserView::new))
                .switchIfEmpty(Mono.defer(() ->
                        userRepository.save(user).map(User::getId)
                                .flatMap(id -> emailRepository.save(new Email(userRegistration.getEmail(), true, id)).doOnNext(
                                        email -> logger.info("Save email '{}' for user {}", email.getEmail(), userRegistration.getLogin())
                                ))
                                .map(Email::getUserId)
                                .flatMap(id -> phonesRepository.save(new Phone(userRegistration.getPhone(), true, id)).doOnNext(
                                        phone -> logger.info("Save phone '{}' for user {}", phone.getNumber(), userRegistration.getLogin())
                                ))
                                .map(Phone::getUserId)
                                .flatMap(id -> bankRepository.save(new BankAccount(userRegistration.getDeposit(), id)).doOnNext(
                                                bankAccount -> logger.info("Save bankAccount for user {}", userRegistration.getLogin()))
                                        .thenReturn(UserMappers.INSTANCE.userView(userRegistration)))));
    }


    @Override
    public Mono<UserView> addUserInfo(UserDtoInfo userDtoInfo, String login) {
        return null;
    }

    @Override
    public Mono<Boolean> addOtherPhone(String phone, String login) {
        return null;
    }

    @Override
    public Mono<Boolean> addOtherEmail(String email, String login) {
        return null;
    }

    @Override
    public Mono<Boolean> deletePhone(String phoneNumber, String login) {
        return null;
    }

    @Override
    public Mono<Boolean> deleteEmail(String email, String login) {
        return null;
    }

    @Override
    public Mono<UserView> searchUser(String search) {
        return null;
    }

    @Override
    public Mono<Boolean> isUserExistByLogin(String username) {
        return null;
    }

    @Override
    public Mono<Boolean> isUserExistByEmail(String email) {
        return null;
    }

    @Override
    public Mono<Boolean> isUserExistByPhoneNumber(String phoneNumber) {
        return null;
    }
}
