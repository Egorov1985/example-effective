package ru.egorov.effectivitereact.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

import static ru.egorov.effectivitereact.util.Constants.USER_PHONE_EXIST;


@Documented
@Constraint(validatedBy = UserPhoneExistValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserPhoneExist {
    String message() default USER_PHONE_EXIST;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
