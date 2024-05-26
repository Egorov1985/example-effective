package ru.egorov.effectiveexample.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import ru.egorov.effectiveexample.service.UserServiceImp;

@RequiredArgsConstructor
public class UserLoginExistValidator implements ConstraintValidator<UserLoginExist, String> {

    private final UserServiceImp userServiceImp;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return !userServiceImp.isUserExistByLogin(name);
    }
}
