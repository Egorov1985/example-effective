package ru.egorov.effectivitereact.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import ru.egorov.effectivitereact.service.imp.UserServiceImp;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;


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
