package ru.egorov.effectiveexample.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import ru.egorov.effectiveexample.service.UserServiceImp;

@RequiredArgsConstructor
public class UserPhoneExistValidator implements ConstraintValidator<UserPhoneExist, String> {

    private final UserServiceImp userServiceImp;
    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        return !userServiceImp.isUserExistByPhoneNumber(phoneNumber);
    }
}
