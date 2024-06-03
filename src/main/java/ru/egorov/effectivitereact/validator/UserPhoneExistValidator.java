package ru.egorov.effectivitereact.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import ru.egorov.effectivitereact.service.imp.UserServiceImp;

@RequiredArgsConstructor
public class UserPhoneExistValidator implements ConstraintValidator<UserPhoneExist, String> {

    private static boolean apply(Boolean aBoolean, boolean b) {
        b = aBoolean;
        return b;
    }

    private final UserServiceImp userServiceImp;
    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        boolean b = true;
        userServiceImp.isUserExistByPhoneNumber(phoneNumber).map(aBoolean -> apply(aBoolean, b)).subscribe();
        return b;
    }
}
