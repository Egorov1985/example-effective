package ru.egorov.effectiveexample.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import ru.egorov.effectiveexample.service.imp.UserServiceImp;

import java.util.concurrent.ExecutionException;


@RequiredArgsConstructor
public class UserEmailExistValidator implements ConstraintValidator<UserEmailExist, String> {

    private final UserServiceImp userServiceImp;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        try {
            return userServiceImp.isUserExistByEmail(email)
                    .map(aBoolean -> !aBoolean)
                    .toFuture()
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
