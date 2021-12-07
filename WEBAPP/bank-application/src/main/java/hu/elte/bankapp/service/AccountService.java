package hu.elte.bankapp.service;

import hu.elte.bankapp.entities.Customer;
import hu.elte.bankapp.entities.PersonalAccount;
import hu.elte.bankapp.entities.SavingAccount;
import hu.elte.bankapp.entities.Account;
import hu.elte.bankapp.entities.User;
import hu.elte.bankapp.models.Status;
import hu.elte.bankapp.repositories.AccountRepository;
import hu.elte.bankapp.repositories.CustomerRepository;
import hu.elte.bankapp.repositories.PersonalAccountRepository;
import hu.elte.bankapp.repositories.SavingAccountRepository;
import hu.elte.bankapp.security.SecurityHelper;
import hu.elte.bankapp.webdomain.AddPersonalAccountView;
import hu.elte.bankapp.webdomain.AddSavingsAccountView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private PersonalAccountRepository personalAccountRepository;

    @Autowired
    private SavingAccountRepository savingAccountRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Iterable<Account> findAllAccounts() {
        return accountRepository.findAll();
    }

    public Iterable<PersonalAccount> findAllPersonalAccounts() {
        return personalAccountRepository.findAll();
    }
    public  Iterable<SavingAccount> findAllSavingAccounts() {
        return savingAccountRepository.findAll();
    }

    public Iterable<PersonalAccount> findAllPersonalAccountsByEmail(String email) {
        return personalAccountRepository.findByUserEmail(email);
    }

    public Iterable<SavingAccount> findAllSavingAccountsByEmail(String email) {
        return savingAccountRepository.findByUserEmail(email);
    }


    public PersonalAccount findPersonalAccountByAccountNumber(String accountNumber) {
        return personalAccountRepository.findByAccountNumber(accountNumber);
    }
    public SavingAccount findSavingAccountByAccountNumber(String accountNumber) {
        return savingAccountRepository.findByAccountNumber(accountNumber);
    }

    public PersonalAccount addPersonalAccount(AddPersonalAccountView accountView) {
        User currentUser = customerRepository.findByEmail(SecurityHelper.getUsername());

        PersonalAccount personalAccount = new PersonalAccount();

        personalAccount.setStatus(Status.ACTIVE);
        personalAccount.setConstructionName(accountView.getName());
        personalAccount.setBalance(0);
        personalAccount.setDailyTransferLimit(accountView.getDailyTransferLimit());
        personalAccount.setAccountNumber(createAccountNumber());
        personalAccount.setUser(currentUser);

        personalAccountRepository.save(personalAccount);

        return personalAccount;
    }

    public SavingAccount addSavingAccount(AddSavingsAccountView accountView) {
        User currentUser = customerRepository.findByEmail(SecurityHelper.getUsername());

        SavingAccount savingAccount = new SavingAccount();

        savingAccount.setStatus(Status.ACTIVE);
        savingAccount.setConstructionName(accountView.getName());
        savingAccount.setBalance(0);
        savingAccount.setAccountNumber(createAccountNumber());
        savingAccount.setUser(currentUser);

        savingAccountRepository.save(savingAccount);

        return savingAccount;
    }

    public PersonalAccount deletePersonalAccount(String accountNumber) {
        PersonalAccount personalAccount = personalAccountRepository.findByAccountNumber(accountNumber);
        personalAccountRepository.delete(personalAccount);

        return personalAccount;
    }

    public SavingAccount deleteSavingsAccount(String accountNumber) {
        SavingAccount savingAccount = savingAccountRepository.findByAccountNumber(accountNumber);
        savingAccountRepository.delete(savingAccount);

        return savingAccount;
    }



    private String createAccountNumber() {
        long lefLimit = 1000000000000000L;
        long rightLimit = 9999999999999999L;

        long generatedLong = generateRandomLong(lefLimit, rightLimit);

        while (!isUnique(String.valueOf(generatedLong))) {
            generatedLong = generateRandomLong(lefLimit, rightLimit);
        }

        return String.valueOf(generatedLong);
    }

    private long generateRandomLong(long lefLimit, long rightLimit) {
        return lefLimit + (long) (Math.random() * (rightLimit - lefLimit));
    }

    private boolean isUnique(String accountNumber) {

        for (PersonalAccount personalAccount : personalAccountRepository.findAll()) {
            if (personalAccount.getAccountNumber().equals(accountNumber)) {
                return false;
            }
        }
        for (SavingAccount savingAccount : savingAccountRepository.findAll()) {
            if (savingAccount.getAccountNumber().equals(accountNumber)) {
                return false;
            }
        }
        return true;
    }


}
