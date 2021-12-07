package hu.elte.bankapp.controllers;

import hu.elte.bankapp.entities.SavingAccount;
import hu.elte.bankapp.security.SecurityHelper;
import hu.elte.bankapp.service.AccountService;
import hu.elte.bankapp.service.TransactionService;
import hu.elte.bankapp.webdomain.AccountView;
import hu.elte.bankapp.webdomain.AddSavingsAccountView;
import hu.elte.bankapp.webdomain.TransactionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SavingsAccountController {

    List<AccountView> savingAccountViews;
    //List<TransactionView> transactionViews;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @RequestMapping("/saving_account")
    public String getSavingAccounts(Model model) {

        model.addAttribute("title", "Saving Accounts");

        List<AccountView> savingAccountViewList = new ArrayList<>();
        accountService.findAllSavingAccountsByEmail(SecurityHelper.getUsername()).forEach(
                p -> savingAccountViewList.add(new AccountView(p.getBalance(), p.getAccountNumber(), p.getConstructionName()))
        );

        if (savingAccountViews == null) {
            savingAccountViews = new ArrayList<>();
            accountService.findAllSavingAccounts().forEach(System.out::println);
            accountService.findAllSavingAccounts().forEach(p -> savingAccountViews.add(new AccountView(p.getBalance(), p.getAccountNumber())));

        }

        List<TransactionView> transactionViews = new ArrayList<>();
        if (accountService.findAllSavingAccountsByEmail(SecurityHelper.getUsername()).iterator().hasNext()) {
            SavingAccount selectedAccount = accountService.findAllSavingAccountsByEmail(SecurityHelper.getUsername()).iterator().next();
            model.addAttribute("selected_account", new AccountView(
                    selectedAccount.getBalance(),
                    selectedAccount.getAccountNumber(),
                    selectedAccount.getConstructionName()));
                    transactionService.findTransactionsByAccountNumber(selectedAccount.getAccountNumber()).forEach(t -> transactionViews.add(new TransactionView(
                        t.getType(), t.getAmount(), t.getComment(), t.getDate(), t.getOwnAccountNumber(), t.getTargetAccountNumber())));

        } else {
            model.addAttribute("selected_account", new AccountView(
                    0,
                    "",
                    "",
                    0));
        }

        model.addAttribute("transactionViews", transactionViews);
        model.addAttribute("accountViews", savingAccountViewList);
        return "saving_accounts.html";
    }

    @GetMapping("/savings_account/{id}")
    public String selectAccount(@PathVariable("id") String id, Model model) {
        model.addAttribute("title", "Personal Accounts");

        List<AccountView> savingsAccountViewList = new ArrayList<>();
        accountService.findAllSavingAccountsByEmail(SecurityHelper.getUsername()).forEach(
                p -> savingsAccountViewList.add(new AccountView(p.getBalance(), p.getAccountNumber(), p.getConstructionName()))
        );

        SavingAccount selectedAccount = accountService.findSavingAccountByAccountNumber(id);

        List<TransactionView> transactionViews = new ArrayList<>();
        transactionService.findTransactionsByAccountNumber(selectedAccount.getAccountNumber()).forEach(t -> transactionViews.add(new TransactionView(
                t.getType(), t.getAmount(), t.getComment(), t.getDate(), t.getOwnAccountNumber(), t.getTargetAccountNumber())));

        model.addAttribute("accountViews", savingsAccountViewList);
        model.addAttribute("selected_account", new AccountView(
                selectedAccount.getBalance(),
                selectedAccount.getAccountNumber(),
                selectedAccount.getConstructionName()));
        model.addAttribute("transactionViews", transactionViews);
        return "saving_accounts.html";
    }

    @RequestMapping("/add_savings_account")
    public String getAddSavingsAccount(Model model) {
        model.addAttribute("title", "Add Savings Account");
        model.addAttribute("account", new AddSavingsAccountView());

        return "add_savings_account.html";
    }

    @PostMapping("/create_savings_account")
    public String addSavingsAccount(@ModelAttribute("account") AddSavingsAccountView accountView, RedirectAttributes redirectAttributes) {

        SavingAccount savingAccount = accountService.addSavingAccount(accountView);
        redirectAttributes.addFlashAttribute("message", "Account successfully created!");
        return "redirect:/saving_account";
    }

    @GetMapping("/delete_savings_account/{id}")
    public String deleteAccount(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {

        SavingAccount savingAccount = accountService.deleteSavingsAccount(id);
        redirectAttributes.addFlashAttribute("message", "Account deleted successfully!");
        return "redirect:/saving_account";
    }

}
