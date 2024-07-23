package ru.egorov.effectiveexample.controller;


import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.egorov.effectiveexample.dto.*;
import ru.egorov.effectiveexample.service.UserServiceImp;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {

    private final UserServiceImp userService;


    @PostMapping("/registration")
    public ResponseEntity<UserView> createUser(@RequestBody @Valid UserRegistration userRegistration) {
        return ResponseEntity.ok().body(userService.createUser(userRegistration));
    }


    @PatchMapping("/add/info")
    public ResponseEntity<UserView> addInfoUser(@RequestBody @Valid UserDtoInfo userDtoInfo, @NonNull Authentication authentication) {
        return ResponseEntity.ok().body(userService.addUserInfo(userDtoInfo, authentication.getPrincipal().toString()));
    }


    @PatchMapping("/add/info/phone")
    public ResponseEntity<String> addUserAdditionalPhone(@NonNull Authentication authentication, @RequestBody @Valid PhoneDto phoneDto) {
        if (userService.addOtherPhone(phoneDto.getPhone(), authentication.getPrincipal().toString())) {
            return ResponseEntity.ok("Success add new phone.");
        }
        return ResponseEntity.badRequest().build();
    }


    @PatchMapping("/add/info/email")
    public ResponseEntity<String> addUserAdditionalEmail(@NonNull Authentication authentication, @RequestBody @Valid EmailDto emailDto) {
        if (userService.addOtherEmail(emailDto.getEmail(), authentication.getPrincipal().toString()))
            return ResponseEntity.ok("Success add new email.");
        return ResponseEntity.badRequest().build();
    }


    @DeleteMapping("/delete/info/phone/{number}")
    public ResponseEntity<String> deleteUserPhone(@NonNull Authentication authentication, @PathVariable String number) {
        if (userService.deletePhone(number, authentication.getPrincipal().toString()))
            return new ResponseEntity<>("Success delete number.", HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @DeleteMapping("/delete/info/mail/{email}")
    public ResponseEntity<String> deleteUserEmail(@NonNull Authentication authentication, @PathVariable String email) {
        if (userService.deleteEmail(email, authentication.getPrincipal().toString()))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/action/search")
    public ResponseEntity<List<UserView>> getUsers(@RequestParam(required = false, defaultValue = "") String search) {
        return ResponseEntity.ok().body(userService.searchUser(search));
    }


}
