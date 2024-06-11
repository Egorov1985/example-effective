package ru.egorov.effectivitereact.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.egorov.effectivitereact.dto.*;
import ru.egorov.effectivitereact.model.Phone;

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
