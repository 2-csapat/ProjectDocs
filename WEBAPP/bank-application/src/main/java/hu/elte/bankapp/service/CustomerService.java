package hu.elte.bankapp.service;

import hu.elte.bankapp.entities.Customer;
import hu.elte.bankapp.exception.UserAlreadyExistAuthenticationException;
import hu.elte.bankapp.repositories.CustomerRepository;
import hu.elte.bankapp.webdomain.RegisterView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

@Service
public class CustomerService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerRepository customerRepository;



    public Customer registerNewUserCustomer(RegisterView customer) {
        if (emailExist(customer.getEmail())) {
            throw new UserAlreadyExistAuthenticationException("There is already an account with that email address: "
                    + customer.getEmail());
        }
        Customer newCustomer = new Customer();

        newCustomer.setEmail(customer.getEmail());
        newCustomer.setName(customer.getName());
        newCustomer.setPhoneNum(customer.getPhoneNum());
        newCustomer.getDateOfBirth(customer.getDateOfBirth());
        newCustomer.setPassword(passwordEncoder.encode(customer.getPassword()));
        newCustomer.setAddress(customer.getAddress());

        customerRepository.save(newCustomer);
        return newCustomer;
    }

    private boolean emailExist(String email) {
        return customerRepository.findByEmail(email) != null;
    }
}
