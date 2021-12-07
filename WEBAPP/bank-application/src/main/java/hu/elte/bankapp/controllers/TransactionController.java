package hu.elte.bankapp.controllers;

import hu.elte.bankapp.configuration.ValidAccount;
import hu.elte.bankapp.entities.Customer;
import hu.elte.bankapp.entities.PersonalAccount;
import hu.elte.bankapp.entities.SimpleTransaction;
import hu.elte.bankapp.entities.SavedTransaction;
import hu.elte.bankapp.exception.UserAlreadyExistAuthenticationException;
import hu.elte.bankapp.repositories.SimpleTransferRepository;
import hu.elte.bankapp.service.TestDataGenerator;
import hu.elte.bankapp.service.TransactionService;
import hu.elte.bankapp.service.AccountService;
import hu.elte.bankapp.webdomain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TransactionController {
    CustomerView customerView;

    List<TransactionView> transactionViews;
    List<TransactionView> simpleTransactionViews;
    List<TransactionView> savedTransactionViews;
    List<TransactionView> regularTransactionViews;

    List<AccountView> personalAccountViews;
    List<AccountView> savingAccountViews;
    List<DirectDebitView> directDebitViews;

    @Autowired
    private SimpleTransferRepository simpleTransferRepository;

    @Autowired
    private TestDataGenerator testDataGenerator;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;


    @PostMapping("/do_transfer")
    public String transfer(Model model, @Valid @ModelAttribute("transaction") TransactionView transaction,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", "Invalid Account");
            return "redirect:/transfer";
        }

        SimpleTransaction simpleTransaction = transactionService.doTransfer(transaction);

        return "redirect:/";
    }

    @PostMapping("/save_transaction")
    public ModelAndView saveTransaction( @ModelAttribute("transaction") TransactionView transaction) {

        SavedTransaction savedTransaction = transactionService.saveTransfer(transaction);

        return new ModelAndView("redirect:/transfer");
    }

    @RequestMapping("/direct_debit")
    public String getDirectDebit(Model model) {

        model.addAttribute("title", "Direct Debit");

        if (directDebitViews == null) {

            directDebitViews = new ArrayList<>();
            transactionService.findAllDirectDebitTransactions().forEach(p -> directDebitViews.add(new DirectDebitView(
                    p.getProviderName(),
                    p.getProviderAccountNumber(),
                    p.getAmount() )));
        }



        model.addAttribute("directDebitView", new DirectDebitView());
        model.addAttribute("directDebitViews", directDebitViews);

        return "direct_debit_module.html";
    }

    @GetMapping("/transfer/{id}")
    public String selectAccount(@PathVariable("id") String id, Model model) {
        model.addAttribute("title", "Transfer Templates");

        List<TransactionView> transactionViews = new ArrayList<>();
        transactionService.findAllSavedTransactions().forEach(
                t -> transactionViews.add(new TransactionView(t.getType(), t.getAmount(), t.getComment(), t.getDate(), t.getOwnAccountNumber(), t.getTargetAccountNumber()))
        );

        model.addAttribute("transactionViews", transactionViews);



        SavedTransaction t = transactionService.findSavedTransactionByComment(id);
        TransactionView transaction = new TransactionView(t.getType(), t.getAmount(), t.getComment(), t.getDate(), t.getOwnAccountNumber(), t.getTargetAccountNumber());
        model.addAttribute("transaction", transaction);

        if (personalAccountViews == null) {
            personalAccountViews = new ArrayList<>();
            accountService.findAllPersonalAccounts().forEach(System.out::println);
            accountService.findAllPersonalAccounts().forEach(p -> personalAccountViews.add(new AccountView(p.getBalance(), p.getAccountNumber())));
        }
        model.addAttribute("personalAccounts", personalAccountViews);


        //model.addAttribute("personalAccounts", personalAccountViews);

        return "transfer.html";
    }

    @GetMapping("/transfer")
    public String getTransfer(Model model) {

        model.addAttribute("title", "Transfer");

        List<TransactionView> transactionViews = new ArrayList<>();
        transactionService.findAllSavedTransactions().forEach(
                t -> transactionViews.add(new TransactionView(t.getType(), t.getAmount(), t.getComment(), t.getDate(), t.getOwnAccountNumber(), t.getTargetAccountNumber()))
        );

        model.addAttribute("transactionViews", transactionViews);

        if (simpleTransactionViews == null) {

            simpleTransactionViews = new ArrayList<>();
            transactionService.findAllSimpleTransactions().forEach(p -> simpleTransactionViews.add(new TransactionView(
                    p.getType(),
                    p.getAmount(),
                    p.getComment(),
                    p.getDate(),
                    p.getOwnAccountNumber(),
                    p.getTargetAccountNumber())));
        }

//        model.addAttribute("transactionViews", simpleTransactionViews);



        TransactionView transaction = new TransactionView();
        model.addAttribute("transaction", transaction);


        List<AccountView> personalAccountViewList = new ArrayList<>();

        accountService.findAllPersonalAccounts().forEach(p -> personalAccountViewList.add(new AccountView(p.getBalance(), p.getAccountNumber())));
        if (personalAccountViews == null) {
//
            personalAccountViews = new ArrayList<>();
            accountService.findAllPersonalAccounts().forEach(System.out::println);
            accountService.findAllPersonalAccounts().forEach(p -> personalAccountViews.add(new AccountView(p.getBalance(), p.getAccountNumber())));
        }
        model.addAttribute("personalAccounts", personalAccountViews);

        return "transfer.html";
    }

    @GetMapping("/future_transaction")
    public String getFutureTransaction(Model model) {

        model.addAttribute("title", "FutureTransfer");

        if (regularTransactionViews == null) {

            regularTransactionViews = new ArrayList<>();
            transactionService.findAllRegularTransactions().forEach(p -> regularTransactionViews.add(new TransactionView(
                    p.getType(),
                    p.getAmount(),
                    p.getComment(),
                    p.getDate(),
                    p.getOwnAccountNumber(),
                    p.getTargetAccountNumber())));
        }

        model.addAttribute("transactionViews", regularTransactionViews);

        return "future_transfer.html";


    }


    @PostMapping("/add_direct_debit")
    public ModelAndView addDirectDebit(@ModelAttribute("directDebit") DirectDebitView directDebitView) {
        directDebitViews.add(directDebitView);
        System.out.println(directDebitView);

        return new ModelAndView("redirect:/direct_debit");
    }


    @PostMapping("/filter")
    public ModelAndView doFilter(@ModelAttribute("account") AccountView accountView) {

        return new ModelAndView("redirect:/history");
    }








}
