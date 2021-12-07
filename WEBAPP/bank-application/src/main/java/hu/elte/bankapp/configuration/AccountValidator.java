package hu.elte.bankapp.configuration;

import hu.elte.bankapp.entities.Account;
import hu.elte.bankapp.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

public class AccountValidator
        implements ConstraintValidator<ValidAccount, String> {

    @Autowired
    private AccountRepository accountRepository;


    @Override
    public void initialize(ValidAccount constraintAnnotation) {
    }

    @Override
    public boolean isValid(String account, ConstraintValidatorContext context) {
        boolean result = Streamable.of(accountRepository.findAll()).stream().anyMatch(elem -> elem.getAccountNumber().equals(account));
        return result ;
    }

    ;


}