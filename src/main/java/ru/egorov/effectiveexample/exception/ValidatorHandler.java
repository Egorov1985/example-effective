package ru.egorov.effectiveexample.exception;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.egorov.effectiveexample.dto.UserRegistration;
import ru.egorov.effectiveexample.repository.EmailsRepository;
import ru.egorov.effectiveexample.repository.PhonesRepository;
import ru.egorov.effectiveexample.repository.UsersRepository;

import java.util.HashSet;
import java.util.Set;



@RequiredArgsConstructor
public class ValidatorHandler  {
    private final Validator validator;
    private final UsersRepository usersRepository;
    private final EmailsRepository emailsRepository;
    private final PhonesRepository phonesRepository;


    public void userValidate(UserRegistration userRegistration) throws  MethodArgumentNotValidException {
        Set<String> errors = new HashSet<>();
        usersRepository.existsUserByLogin(userRegistration.getLogin())
                .subscribe(aBoolean -> {
                    if (aBoolean)
                        errors.add("User with login exists!");
                });
        emailsRepository.existsEmailByEmail(userRegistration.getEmail())
                .subscribe(aBoolean -> {
                    if (aBoolean)
                        errors.add("User with emails exists!");
                });
        phonesRepository.existsPhonesByNumber(userRegistration.getPhone())
                .subscribe(aBoolean -> {
                    if (aBoolean)
                        errors.add("User with phone number exists!");
                });
    }


    public <T> void validate(T o) {
        Set<ConstraintViolation<T>> validate = validator.validate(o);

        if (!validate.isEmpty()) {
            //ConstraintViolation<T> violation = validate.stream().iterator().next();
            throw new ConstraintViolationException(validate);
        }
    }
}
