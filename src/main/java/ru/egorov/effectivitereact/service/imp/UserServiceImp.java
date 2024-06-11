package ru.egorov.effectivitereact.service.imp;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.egorov.effectivitereact.dto.UserDtoInfo;
import ru.egorov.effectivitereact.dto.UserRegistration;
import ru.egorov.effectivitereact.dto.UserView;
import ru.egorov.effectivitereact.dto.mappers.UserMappers;
import ru.egorov.effectivitereact.exception.ResourceException;
import ru.egorov.effectivitereact.exception.UserNotFoundException;
import ru.egorov.effectivitereact.model.BankAccount;
import ru.egorov.effectivitereact.model.Email;
import ru.egorov.effectivitereact.model.Phone;
import ru.egorov.effectivitereact.model.User;
import ru.egorov.effectivitereact.repository.BankAccountRepository;
import ru.egorov.effectivitereact.repository.EmailsRepository;
import ru.egorov.effectivitereact.repository.PhonesRepository;
import ru.egorov.effectivitereact.repository.UsersRepository;
import ru.egorov.effectivitereact.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

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
    public Mono<UserView> createUser(@Valid UserRegistration userRegistration) {

        return Mono.just(userRegistration)
                .doOnNext(userReg -> {
                    saveUser(userRegistration);
                    logger.info("Create new User with {}!", userReg.getLogin());
                })
                .thenReturn(UserMappers.INSTANCE.userView(userRegistration));
    }


    @Override
    public Mono<UserView> addUserInfo(UserDtoInfo userDtoInfo, String login) {
        return userRepository.findUsersByLogin(login)
                .flatMap(user -> {
                    if (!user.getFirstName().isBlank() && !user.getMiddleName().isBlank() &&
                            !user.getLastName().isBlank() && user.getBirthday() != null)
                        return Mono.error(new ResourceException("Информация о пользователи уже добавлена!"));
                    user.setFirstName(userDtoInfo.getFirstName());
                    user.setLastName(userDtoInfo.getLastName());
                    user.setMiddleName(userDtoInfo.getMiddleName());
                    user.setBirthday(LocalDate.parse(userDtoInfo.getBirthday(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                    return userRepository.save(user);
                })
                .doOnError(e -> logger.info("Повторная попытка добавления информации о пользователе."))
                .map(UserMappers.INSTANCE::userView)
                .switchIfEmpty(Mono.error(new UserNotFoundException("Ошибка добавление информации! Пользователь с логином "
                        + login + " не найден!")));
    }

    @Override
    public Mono<Boolean> addOtherPhone(String number, String login) {
        return phonesRepository.existsPhonesByNumber(number)
                .flatMap(aBoolean -> {
                    if (!aBoolean){
                      return  phonesRepository.save(new Phone(number, false, "d9adff87-3c89-4c54-af70-ba2e0b2b9e9d"))
                              .flatMap(phone -> Mono.just(true));

                    }
                    return Mono.just(false);
                });

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
