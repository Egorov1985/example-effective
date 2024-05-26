package ru.egorov.effectiveexample.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static ru.egorov.effectiveexample.util.Constants.USER_LOGIN_EXIST;

@Documented
@Constraint(validatedBy = UserLoginExistValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface UserLoginExist {

    String message() default USER_LOGIN_EXIST;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
