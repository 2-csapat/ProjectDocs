package hu.elte.bankapp;

import hu.elte.bankapp.entities.Customer;
import hu.elte.bankapp.repositories.CustomerRepository;
import hu.elte.bankapp.repositories.PersonalAccountRepository;
import hu.elte.bankapp.repositories.SavingAccountRepository;
import hu.elte.bankapp.service.TestDataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.SpringVersion;

import java.util.Scanner;

@SpringBootApplication
public class BankAppApplication {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    PersonalAccountRepository personalAccountRepository;
    @Autowired
    SavingAccountRepository savingAccountRepository;
    @Autowired
    TestDataGenerator testDataGenerator;

    public static void main(String[] args) {
        SpringApplication.run(BankAppApplication.class, args);
        System.out.println("version: " + SpringVersion.getVersion());
    }

    @Bean
    public CommandLineRunner runner(ApplicationContext ctx) {
        return valami -> {
            testDataGenerator.createTestData();
            System.out.println("-----------------------");

            customerRepository.findAll().forEach(System.out::println);
            personalAccountRepository.findAll().forEach(System.out::println);
            savingAccountRepository.findAll().forEach(System.out::println);
        };
    }
}
