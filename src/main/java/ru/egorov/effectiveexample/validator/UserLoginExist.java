package ru.egorov.effectiveexample.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

import static ru.egorov.effectiveexample.util.Constants.USER_LOGIN_EXIST;

@Documented
@Constraint(validatedBy = {UserLoginExistValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserLoginExist {

    String message() default USER_LOGIN_EXIST;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
