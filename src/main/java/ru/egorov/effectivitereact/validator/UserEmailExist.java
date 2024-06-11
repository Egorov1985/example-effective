package ru.egorov.effectivitereact.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static ru.egorov.effectivitereact.util.Constants.USER_EMAIL_EXIST;


@Documented
@Constraint(validatedBy = {UserEmailExistValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserEmailExist {

    String message() default USER_EMAIL_EXIST;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
