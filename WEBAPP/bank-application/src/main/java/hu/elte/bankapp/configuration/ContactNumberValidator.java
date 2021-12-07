package hu.elte.bankapp.configuration;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContactNumberValidator implements ConstraintValidator<PhoneValidator, String> {

    @Override
    public boolean isValid(String contactField, ConstraintValidatorContext cxt) {
        return contactField != null &&
                contactField.matches("^\\+36 \\(\\d{1,2}\\) \\d{3}\\-\\d{3,4}$");
    }

}