package ru.egorov.effectiveexample.service;

import reactor.core.publisher.Mono;
import ru.egorov.effectiveexample.dto.*;

public interface UserService {
    Mono<UserView> createUser(UserRegistration userRegistration);

    Mono<UserView> addUserInfo(UserDtoInfo userDtoInfo, String login);

    Mono<Boolean> addOtherPhone(PhoneDto phone, String login);

    Mono<Boolean> addOtherEmail(EmailDto email, String login);

    Mono<Boolean> deletePhone(String phoneNumber, String login);

    Mono<Boolean> deleteEmail(String email, String login);

    Mono<UserView> searchUser(String search);

    Mono<Boolean> isUserExistByLogin(String username);

    Mono<Boolean> isUserExistByEmail(String email);

    Mono<Boolean> isUserExistByPhoneNumber(String phoneNumber);
}
