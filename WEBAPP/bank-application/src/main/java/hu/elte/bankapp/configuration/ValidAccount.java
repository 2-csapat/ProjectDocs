package hu.elte.bankapp.configuration;



import java.lang.annotation.*;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AccountValidator.class)
@Documented
public @interface ValidAccount {
    String message() default "Invalid account";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
