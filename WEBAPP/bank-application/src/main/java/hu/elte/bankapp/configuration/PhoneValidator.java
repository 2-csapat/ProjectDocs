package hu.elte.bankapp.configuration;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = ContactNumberValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneValidator {

    String message() default "Invalid phone number. Pattern: +36 (10) 111-1111";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
