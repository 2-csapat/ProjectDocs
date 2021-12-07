package hu.elte.bankapp.service;

import hu.elte.bankapp.entities.*;
import hu.elte.bankapp.models.Status;
import hu.elte.bankapp.repositories.CustomerRepository;
import hu.elte.bankapp.repositories.PersonalAccountRepository;
import hu.elte.bankapp.repositories.SavingAccountRepository;

import hu.elte.bankapp.repositories.SimpleTransferRepository;
import hu.elte.bankapp.repositories.SavedTransferRepository;
import hu.elte.bankapp.repositories.RegularTransferRepository;
import hu.elte.bankapp.repositories.DirectDebitTransferRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class TestDataGenerator {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PersonalAccountRepository personalAccountRepository;

    @Autowired
    private SavingAccountRepository savingAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SimpleTransferRepository simpleTransferRepository;

    @Autowired
    private SavedTransferRepository savedTransferRepository;

    @Autowired
    private RegularTransferRepository regularTransferRepository;

    @Autowired
    private DirectDebitTransferRepository directDebitTransferRepository;

    @Transactional
    public void createTestData() {
        Customer bela = createCustomer("Bela", "1234","bela@nagyonjo.hu", "0630","address", LocalDate.now());


        PersonalAccount pAccount1 = createPersonalAccount( bela, 50000, "HU123456789P", "PersonalAccount1", Status.ACTIVE, 10000);
        PersonalAccount pAccount2 = createPersonalAccount(bela,100000, "HU121212121S", "PersonalAccount2", Status.ACTIVE, 20000);
        PersonalAccount pAccount3 = createPersonalAccount(bela,100000, "HU123412341J", "PersonalAccount3", Status.INACTIVE, 5000);

        SavingAccount account2 = createSavingAccount(bela,20000, "HU999999999S", "whatsthis", Status.ACTIVE);

//        List<Account> accounts = Arrays.asList(pAccount1,pAccount2,pAccount3);
//        bela.setAccounts(accounts);

        personalAccountRepository.save(pAccount1);
        personalAccountRepository.save(pAccount2);
        personalAccountRepository.save(pAccount3);
        savingAccountRepository.save(account2);
        customerRepository.save(bela);


        SavedTransaction savedTransaction1 = createSavedTransaction( 41200512, "Savingre", LocalDateTime.of(2019, 03, 28, 14, 33, 48, 000000001), "HU121212121P", "HU999999999S", "SAVED");
        SimpleTransaction simpleTransaction1 = createSimpleTransaction( 6000, "Savingrol", LocalDateTime.of(2021, 12, 3, 13, 0, 0, 000000001), "HU999999999S", "HU121212121P", "SIMPLE");
        RegularTransaction regularTransaction1 = createRegularTransaction( 15000, "PocketMoney", LocalDateTime.of(2021, 12, 10, 8, 15, 32, 033011212), "HU123456789P", "HU121212121P", "REGULAR");
        DirectDebitTransaction directDebitTransaction1 = createDirectDebitTransaction( "Satoshi Nakamoto", "4654984632134946", 18000000);


        savedTransferRepository.save(savedTransaction1);
        simpleTransferRepository.save(simpleTransaction1);
        regularTransferRepository.save(regularTransaction1);
        directDebitTransferRepository.save(directDebitTransaction1);

    }

    private Customer createCustomer(String name, String password, String email, String phoneNum, String address, LocalDate dateOfBirth) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setPassword(passwordEncoder.encode(password));
        System.out.println(customer.getPassword());
        customer.setEmail(email);
        customer.setPhoneNum(phoneNum);
        customer.setAddress(address);
        customer.setDateOfBirth(dateOfBirth);

        return customer;
    }

    private PersonalAccount createPersonalAccount(User user, int balance, String accountNumber, String constructionName, Status status, int dailyTransferLimit) {
        PersonalAccount personalAccount = new PersonalAccount();
        personalAccount.setUser(user);
        personalAccount.setBalance(balance);
        personalAccount.setAccountNumber(accountNumber);
        personalAccount.setConstructionName(constructionName);
        personalAccount.setStatus(status);
        personalAccount.setDailyTransferLimit(dailyTransferLimit);
        return personalAccount;
    }

    private SavingAccount createSavingAccount(User user, int balance, String accountNumber, String constructionName, Status status) {
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setBalance(balance);
        savingAccount.setAccountNumber(accountNumber);
        savingAccount.setConstructionName(constructionName);
        savingAccount.setStatus(status);
        savingAccount.setUser(user);
        return savingAccount;
    }

    public SimpleTransaction createSimpleTransaction(int amount, String comment, LocalDateTime date, String ownAccountNubmer, String targetAccountNumber, String type) {
        SimpleTransaction simpleTransaction = new SimpleTransaction();
        simpleTransaction.setAmount(amount);
        simpleTransaction.setComment(comment);
        simpleTransaction.setDate(date);
        simpleTransaction.setOwnAccountNumber(ownAccountNubmer);
        simpleTransaction.setTargetAccountNumber(targetAccountNumber);
        simpleTransaction.setType(type);
        return simpleTransaction;
    }
    private SavedTransaction createSavedTransaction(int amount, String comment, LocalDateTime date, String ownAccountNubmer, String targetAccountNumber, String type) {
        SavedTransaction savedTransaction = new SavedTransaction();
        savedTransaction.setAmount(amount);
        savedTransaction.setComment(comment);
        savedTransaction.setDate(date);
        savedTransaction.setOwnAccountNumber(ownAccountNubmer);
        savedTransaction.setTargetAccountNumber(targetAccountNumber);
        savedTransaction.setType(type);
        return savedTransaction;
    }

    private RegularTransaction createRegularTransaction(int amount, String comment, LocalDateTime date, String ownAccountNubmer, String targetAccountNumber, String type) {
        RegularTransaction regularTransaction = new RegularTransaction();
        regularTransaction.setAmount(amount);
        regularTransaction.setComment(comment);
        regularTransaction.setDate(date);
        regularTransaction.setOwnAccountNumber(ownAccountNubmer);
        regularTransaction.setTargetAccountNumber(targetAccountNumber);
        regularTransaction.setType(type);
        return regularTransaction;
    }

    private DirectDebitTransaction createDirectDebitTransaction(String providerName, String providerAccountNumber, int amount) {
        DirectDebitTransaction directDebitTransaction = new DirectDebitTransaction();
        directDebitTransaction.setProviderName(providerName);
        directDebitTransaction.setProviderAccountNumber(providerAccountNumber);
        directDebitTransaction.setAmount(amount);
        return directDebitTransaction;
    }


}
