package ru.egorov.effectiveexample.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import ru.egorov.effectiveexample.service.UserServiceImp;

@RequiredArgsConstructor
public class UserEmailExistValidator implements ConstraintValidator<UserEmailExist, String> {

    private final UserServiceImp userServiceImp;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return !userServiceImp.isUserExistByEmail(email);
    }
}
