package ru.egorov.effectivitereact.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import ru.egorov.effectivitereact.service.imp.UserServiceImp;


@RequiredArgsConstructor
public class UserEmailExistValidator implements ConstraintValidator<UserEmailExist, String> {

    private final UserServiceImp userServiceImp;

    private static boolean apply(Boolean aBoolean) {
        return aBoolean;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
       return Boolean.TRUE.equals(userServiceImp.isUserExistByEmail(email).block());
    }
}
