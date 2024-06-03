package ru.egorov.effectivitereact.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import ru.egorov.effectivitereact.service.imp.UserServiceImp;

@RequiredArgsConstructor
public class UserLoginExistValidator implements ConstraintValidator<UserLoginExist, String> {

    private final UserServiceImp userServiceImp;

    private static boolean apply(Boolean aBoolean, boolean b) {
        b = aBoolean;
        return b;
    }


    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        boolean b = true;
        userServiceImp.isUserExistByLogin(name).map(aBoolean -> apply(aBoolean, b)).subscribe();
        return !b;
    }
}
