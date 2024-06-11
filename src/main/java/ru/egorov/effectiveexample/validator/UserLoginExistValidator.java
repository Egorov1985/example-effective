package ru.egorov.effectivitereact.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import ru.egorov.effectivitereact.service.imp.UserServiceImp;

import java.util.concurrent.ExecutionException;


@RequiredArgsConstructor
public class UserLoginExistValidator implements ConstraintValidator<UserLoginExist, String> {

    private final UserServiceImp userServiceImp;

    @Override
    public boolean isValid(String login, ConstraintValidatorContext constraintValidatorContext) {
        try {
            return userServiceImp.isUserExistByLogin(login)
                    .map(aBoolean -> !aBoolean)
                    .toFuture()
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
