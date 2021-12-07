package hu.elte.bankapp.controllers;

import hu.elte.bankapp.entities.PersonalAccount;
import hu.elte.bankapp.security.SecurityHelper;
import hu.elte.bankapp.service.AccountService;
import hu.elte.bankapp.service.TransactionService;
import hu.elte.bankapp.webdomain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AccountsController {

    List<TransactionView> transactionViews;
    List<TransactionView> savedTransactionViews;
    List<AccountView> accountViews;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @RequestMapping("/personal_account")
    public String getPersonalAccounts(Model model) {
        model.addAttribute("title", "Personal Accounts");


        List<AccountView> personalAccountViewList = new ArrayList<>();
        accountService.findAllPersonalAccountsByEmail(SecurityHelper.getUsername()).forEach(
                p -> personalAccountViewList.add(new AccountView(p.getBalance(), p.getAccountNumber(), p.getConstructionName(), p.getDailyTransferLimit()))
        );

        List<TransactionView> transactionViews = new ArrayList<>();
        if (accountService.findAllPersonalAccountsByEmail(SecurityHelper.getUsername()).iterator().hasNext()) {
            PersonalAccount selectedAccount = accountService.findAllPersonalAccountsByEmail(SecurityHelper.getUsername()).iterator().next();
            model.addAttribute("selected_account", new AccountView(
                    selectedAccount.getBalance(),
                    selectedAccount.getAccountNumber(),
                    selectedAccount.getConstructionName(),
                    selectedAccount.getDailyTransferLimit()));
                    transactionService.findTransactionsByAccountNumber(selectedAccount.getAccountNumber()).forEach(t -> transactionViews.add(new TransactionView(
                            t.getType(), t.getAmount(), t.getComment(), t.getDate(), t.getOwnAccountNumber(), t.getTargetAccountNumber())));
        } else {
            model.addAttribute("selected_account", new AccountView(
                    0,
                    "",
                    "",
                    0));
        }

        if (savedTransactionViews == null) {

            savedTransactionViews = new ArrayList<>();
            transactionService.findAllSavedTransactions().forEach(p -> savedTransactionViews.add(new TransactionView(
                    p.getType(),
                    p.getAmount(),
                    p.getComment(),
                    p.getDate(),
                    p.getOwnAccountNumber(),
                    p.getTargetAccountNumber())));
        }

        model.addAttribute("accountViews", personalAccountViewList);
        model.addAttribute("transactionViews", transactionViews);

        //deprecated
//        model.addAttribute("account", new AccountView());

        return "personal_accounts.html";
    }

    @GetMapping("/personal_account/{id}")
    public String selectAccount(@PathVariable("id") String id, Model model) {
        model.addAttribute("title", "Personal Accounts");

        List<AccountView> personalAccountViewList = new ArrayList<>();
        accountService.findAllPersonalAccountsByEmail(SecurityHelper.getUsername()).forEach(
                p -> personalAccountViewList.add(new AccountView(p.getBalance(), p.getAccountNumber(), p.getConstructionName(), p.getDailyTransferLimit()))
        );

        PersonalAccount selectedAccount = accountService.findPersonalAccountByAccountNumber(id);

        List<TransactionView> transactionViews = new ArrayList<>();
        transactionService.findTransactionsByAccountNumber(selectedAccount.getAccountNumber()).forEach(t -> transactionViews.add(new TransactionView(
                t.getType(), t.getAmount(), t.getComment(), t.getDate(), t.getOwnAccountNumber(), t.getTargetAccountNumber())));

        model.addAttribute("accountViews", personalAccountViewList);
        model.addAttribute("selected_account", new AccountView(
                selectedAccount.getBalance(),
                selectedAccount.getAccountNumber(),
                selectedAccount.getConstructionName(),
                selectedAccount.getDailyTransferLimit()));
        model.addAttribute("transactionViews", transactionViews);

        model.addAttribute("test", id);

        return "personal_accounts.html";
    }

    @RequestMapping("/add_personal_account")
    public String getAddAccount(Model model ) {
        model.addAttribute("title", "Add Personal Account");
        model.addAttribute("account", new AddPersonalAccountView());

        return "add_personal_account.html";
    }

    @PostMapping("/add_account")
    public String addAccount(@ModelAttribute("account") AddPersonalAccountView account, RedirectAttributes redirectAttributes) {

        PersonalAccount personalAccount = accountService.addPersonalAccount(account);
        redirectAttributes.addFlashAttribute("message", "Account successfully created!");
        return "redirect:/personal_account";
    }

    @GetMapping("/delete_account/{id}")
    public String deleteAccount(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {

        PersonalAccount personalAccount = accountService.deletePersonalAccount(id);
        redirectAttributes.addFlashAttribute("message", "Account deleted successfully!");
        return "redirect:/personal_account";
    }

    @RequestMapping("/account_history")
    public String getAccountHistory(Model model) {
        model.addAttribute("title", "Account history");
        if(transactionViews == null) {

            transactionViews = new ArrayList<>();
            transactionService.findAllTransactions().forEach(p -> transactionViews.add(new TransactionView(
                    p.getType(),
                    p.getAmount(),
                    p.getComment(),
                    p.getDate(),
                    p.getOwnAccountNumber(),
                    p.getTargetAccountNumber())));
        }

        List<AccountView> accountViewList = new ArrayList<>();
        accountService.findAllAccounts().forEach(p -> accountViewList.add(new AccountView(p.getBalance(), p.getAccountNumber())));

        if (accountViews == null) {
//
            accountViews = new ArrayList<>();
            accountService.findAllAccounts().forEach(System.out::println);
            accountService.findAllAccounts().forEach(p -> accountViews.add(new AccountView(p.getBalance(), p.getAccountNumber())));
        }
        model.addAttribute("account", new AccountView(123, "9809890"));
        model.addAttribute("accounts", accountViews);
        model.addAttribute("transactions", transactionViews);

        return "account_history_module.html";
    }





}
