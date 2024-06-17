package ru.egorov.effectiveexample.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.egorov.effectiveexample.dto.*;

public interface UserService {
    Mono<UserView> createUser(UserRegistration userRegistration);

    Mono<UserView> addUserInfo(UserDto userDto, String login);

    Mono<Boolean> addOtherPhone(PhoneDto phone, String login);

    Mono<Boolean> addOtherEmail(EmailDto email, String login);

    Mono<Boolean> deletePhone(PhoneDto phone, String login);

    Mono<Boolean> deleteEmail(EmailDto email, String login);

    Flux<UserView> searchUser(UserRequest request);

    Mono<Boolean> isUserExistByLogin(String username);

    Mono<Boolean> isUserExistByEmail(String email);

    Mono<Boolean> isUserExistByPhoneNumber(String phoneNumber);
}
