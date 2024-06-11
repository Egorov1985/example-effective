package ru.egorov.effectiveexample.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import ru.egorov.effectiveexample.service.imp.UserServiceImp;

import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
public class UserPhoneExistValidator implements ConstraintValidator<UserPhoneExist, String> {


    private final UserServiceImp userServiceImp;

    @Override
    public void initialize(UserPhoneExist constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String number, ConstraintValidatorContext constraintValidatorContext) {
        try {
            return userServiceImp.isUserExistByPhoneNumber(number)
                    .map(aBoolean -> !aBoolean)
                    .toFuture()
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
