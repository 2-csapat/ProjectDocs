package hu.elte.bankapp.controllers;

import hu.elte.bankapp.entities.Customer;
import hu.elte.bankapp.exception.UserAlreadyExistAuthenticationException;
import hu.elte.bankapp.service.AccountService;
import hu.elte.bankapp.service.CustomerService;
import hu.elte.bankapp.service.TransactionService;
import hu.elte.bankapp.service.UserService;
import hu.elte.bankapp.webdomain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {


    @Autowired
    CustomerService customerService;
    private ObjectError elem;

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;

    @RequestMapping("")
    public String loadContent(Model model) {
        model.addAttribute("title", "Home");

            List<TransactionView> transactionViews = new ArrayList<>();
            transactionService.findAllTransactions().forEach(p -> transactionViews.add(new TransactionView(p.getType(), p.getAmount(), p.getComment(), p.getDate(), p.getOwnAccountNumber() ,p.getTargetAccountNumber())));
            
        model.addAttribute("transactionViews", transactionViews);

        return "index.html";
    }

    @GetMapping("/register")
    public String loadRegister(Model model) {
        model.addAttribute("title", "Registration");
        model.addAttribute("customer", new RegisterView());

        return "registration.html";
    }
    @PostMapping("/register")
    public String register(Model model, @Valid @ModelAttribute("customer") RegisterView customer ,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if(customer.getPassword() != null && customer.getRePassword() != null){
            if(!customer.getPassword().equals(customer.getRePassword())){
                bindingResult.addError(new FieldError("customer", "rePassword", "Passwords do not match"));
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("customer",customer);
           return "registration.html";
        }
        try {
            Customer userCustomer = customerService.registerNewUserCustomer(customer);
        } catch (UserAlreadyExistAuthenticationException e) {
            model.addAttribute("customer", customer);
            model.addAttribute("message", "An account for that username/email already exists.");
            return "registration.html";
        }

        redirectAttributes.addFlashAttribute("message", "Customer successfully saved.");

        return "redirect:/login";
    }



    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Login");
        return "login.html";
    }

    // Login form with error
    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login.html";
    }

}
