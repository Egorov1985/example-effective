package ru.egorov.effectiveexample.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.egorov.effectiveexample.dto.*;
import ru.egorov.effectiveexample.service.imp.UserServiceImp;
import ru.egorov.effectiveexample.swagger.UserSwaggerController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController implements UserSwaggerController {

    private final UserServiceImp userService;


    @PostMapping("/registration")
    public Mono<ResponseEntity<UserView>> createUser(@Valid @RequestBody UserRegistration userRegistration) {
        return userService.createUser(userRegistration)
                .map(userView -> ResponseEntity.ok().body(userView));
    }


    @PatchMapping("/add/info")
    public Mono<ResponseEntity<UserView>> addInfoUser(@RequestBody @Valid UserDtoInfo userDtoInfo, String login) {
        return userService.addUserInfo(userDtoInfo, "login3333")
                .map(userView -> ResponseEntity.ok().body(userView));
    }



    @PatchMapping("/add/info/phone")
    public Mono<ResponseEntity<String>> addUserAdditionalPhone(String login, @RequestBody @Valid PhoneDto phone) {
        return userService.addOtherPhone(phone, "login3333")
                .map(aBoolean -> {
                    if (aBoolean)
                        return ResponseEntity.ok().body("Телефон успешного добавлен!");
                    return ResponseEntity.badRequest().body("Телефон уже добавлен или занят другим пользователем!");
                });
    }


    @PatchMapping("/add/info/email")
    public Mono<ResponseEntity<String>> addUserAdditionalEmail(//@NonNull Authentication authentication,
                                                               @RequestBody @Valid EmailDto email) {
        return userService.addOtherEmail(email, "login3333")
                .map(aBoolean -> {
                    if (aBoolean)
                        return ResponseEntity.ok().body("Дополнительная электронная почта добавлена!");
                    return ResponseEntity.badRequest().body("Почта уже добавлена или занята другим пользователем!");
                });

    }




    @DeleteMapping("/delete/info/phone")
    public Mono<ResponseEntity<String>> deleteUserPhone(//    @NonNull Authentication authentication,
                                                        @RequestBody PhoneDto phone) {
        return userService.deletePhone(phone, "login3333")
                .map(aBoolean -> {
                    if (aBoolean)
                        return new ResponseEntity<>(HttpStatus.OK);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                });
    }


    @DeleteMapping("/delete/info/mail")
    public Mono<ResponseEntity<String>> deleteUserEmail(//@NonNull Authentication authentication,
                                                        @RequestBody EmailDto email) {
        return userService.deleteEmail(email, "login3333")
                .map(aBoolean -> {
                    if (aBoolean)
                        return new ResponseEntity<>(HttpStatus.OK);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                });

    }
    @Override
    public Mono<ResponseEntity<List<UserView>>> getUsers(String search) {
        return null;
    }

}
