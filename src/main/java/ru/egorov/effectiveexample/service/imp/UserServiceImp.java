package ru.egorov.effectiveexample.service.imp;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.egorov.effectiveexample.dto.*;
import ru.egorov.effectiveexample.exception.ResourceException;
import ru.egorov.effectiveexample.exception.UserNotFoundException;
import ru.egorov.effectiveexample.mappers.UserMappers;
import ru.egorov.effectiveexample.model.BankAccount;
import ru.egorov.effectiveexample.model.Email;
import ru.egorov.effectiveexample.model.Phone;
import ru.egorov.effectiveexample.model.User;
import ru.egorov.effectiveexample.repository.BankAccountRepository;
import ru.egorov.effectiveexample.repository.EmailsRepository;
import ru.egorov.effectiveexample.repository.PhonesRepository;
import ru.egorov.effectiveexample.repository.UsersRepository;
import ru.egorov.effectiveexample.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

        return Mono.just(userRegistration)
                .doOnNext(userReg -> {
                    saveUser(userRegistration);
                    logger.info("Create new User with {}!", userReg.getLogin());
                })
                .thenReturn(UserMappers.INSTANCE.userView(userRegistration));
    }


    @Override
    public Mono<UserView> addUserInfo(UserDto userDto, String login) {
        return userRepository.findUsersByLogin(login)
                .flatMap(user -> {
                    if (user.getFirstName() != null && user.getMiddleName() != null &&
                            user.getLastName() != null && user.getBirthday() != null)
                        return Mono.error(new ResourceException("Информация о пользователи уже добавлена!"));
                    user.setFirstName(userDto.getFirstName());
                    user.setLastName(userDto.getLastName());
                    user.setMiddleName(userDto.getMiddleName());
                    user.setBirthday(LocalDate.parse(userDto.getBirthday(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                    return userRepository.save(user);
                })
                .doOnError(e -> logger.info("Повторная попытка добавления информации о пользователе."))
                .map(UserMappers.INSTANCE::userView)
                .switchIfEmpty(Mono.error(new UserNotFoundException("Ошибка добавление информации! Пользователь с логином "
                        + login + " не найден!")));
    }

    @Override
    public Mono<Boolean> addOtherPhone(PhoneDto phone, String login) {
        return phonesRepository.existsPhonesByNumber(phone.getPhone())
                .flatMap(aBoolean -> {
                    if (!aBoolean) {
                        return userRepository.findUsersByLogin(login)
                                .flatMap(user ->
                                        phonesRepository.save(new Phone(phone.getPhone(),
                                                        false, user.getId()))
                                                .map(ph -> true)
                                );
                    }
                    return Mono.just(false);
                });

    }

    @Override
    public Mono<Boolean> addOtherEmail(EmailDto email, String login) {
        return emailRepository.existsEmailByEmail(email.getEmail())
                .flatMap(aBoolean -> {
                    if (!aBoolean) {
                        return userRepository.findUsersByLogin(login)
                                .flatMap(user ->
                                        emailRepository.save(new Email(email.getEmail(),
                                                        false, user.getId()))
                                                .map(e -> true)
                                );
                    }
                    return Mono.just(false);
                });
    }

    @Override
    public Mono<Boolean> deletePhone(PhoneDto phone, String login) {
        return userRepository.findUsersByLogin(login)
                .flatMap(user -> phonesRepository.countPhoneByUserId(user.getId())
                        .flatMap(count ->
                                phonesRepository.existsPhonesByNumber(phone.getPhone())
                                        .flatMap(aBoolean -> {
                                            if (!aBoolean)
                                                return Mono.just(false);
                                            if (count == 1)
                                                return Mono.error(new ResourceException("Нельзя удалить последний номер телефона для аккаунта!"));
                                            return phonesRepository.deletePhoneByNumber(phone.getPhone()).flatMap(v -> Mono.just(true));
                                        })
                        ));
    }

    @Override
    public Mono<Boolean> deleteEmail(EmailDto email, String login) {
        return userRepository.findUsersByLogin(login)
                .flatMap(user -> emailRepository.countEmailsByUserId(user.getId())
                        .flatMap(count ->
                                emailRepository.existsEmailByEmail(email.getEmail())
                                        .flatMap(aBoolean -> {
                                            if (!aBoolean)
                                                return Mono.just(false);
                                            if (count == 1)
                                                return Mono.error(new ResourceException("Нельзя удалить последний электронный адрес для аккаунта!"));
                                            return emailRepository.deleteEmailsByEmail(email.getEmail()).flatMap(v -> Mono.just(true));
                                        })
                        ));
    }

    @Override
    public Flux<UserView> searchUser(UserRequest request) {

        if (request.getBirthday()!=null)
            return userRepository.findByBirthdayLessThan(LocalDate.parse(request.getBirthday(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .map(UserMappers.INSTANCE::userView);
        if (request.getEmail()!=null)
            return emailRepository.getIdFromEmail(request.getEmail()).flatMap(userRepository::findById)
                    .map(UserMappers.INSTANCE::userView).flux();
        if (request.getPhone()!=null)
            return phonesRepository.getUserIdFrom(request.getPhone()).flatMap(userRepository::findById)
                    .map(UserMappers.INSTANCE::userView).flux();
        return userRepository.findAll(Example.of(UserMappers.INSTANCE.user(request),
                ExampleMatcher.matchingAll().withIgnoreCase(true).withIgnoreNullValues())).map(UserMappers.INSTANCE::userView);

    }

    @Override
    public Mono<Boolean> isUserExistByLogin(String login) {
        return userRepository.existsUserByLogin(login);
    }

    @Override
    public Mono<Boolean> isUserExistByEmail(String email) {
        return emailRepository.existsEmailByEmail(email);
    }

    @Override
    public Mono<Boolean> isUserExistByPhoneNumber(String phoneNumber) {
        return phonesRepository.existsPhonesByNumber(phoneNumber);
    }

    private void saveUser(UserRegistration userRegistration) {
        User user = new User();
        user.setLogin(userRegistration.getLogin());
        user.setPassword(userRegistration.getPassword());
        userRepository.save(user).map(User::getId)
                .flatMap(id -> emailRepository.save(new Email(userRegistration.getEmail(), true, id)).doOnNext(
                        email -> logger.info("Save email '{}' for user {}", email.getEmail(), userRegistration.getLogin())
                ))
                .map(Email::getUserId)
                .flatMap(id -> phonesRepository.save(new Phone(userRegistration.getPhone(), true, id)).doOnNext(
                        phone -> logger.info("Save phone '{}' for user {}", phone.getNumber(), userRegistration.getLogin())
                ))
                .map(Phone::getUserId)
                .subscribe(id -> bankRepository.save(new BankAccount(userRegistration.getDeposit(), id)).subscribe(
                        bankAccount -> logger.info("Save bankAccount for user {}", userRegistration.getLogin())));
    }
}
