package hu.elte.bankapp.service;

import hu.elte.bankapp.entities.*;
import hu.elte.bankapp.repositories.TransferRepository;
import hu.elte.bankapp.repositories.SavedTransferRepository;
import hu.elte.bankapp.repositories.SimpleTransferRepository;
import hu.elte.bankapp.repositories.RegularTransferRepository;
import hu.elte.bankapp.repositories.DirectDebitTransferRepository;
import hu.elte.bankapp.webdomain.TransactionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class TransactionService {
    @Autowired
    private  TransferRepository transferRepository;
    @Autowired
    private SavedTransferRepository savedTransferRepository;
    @Autowired
    private SimpleTransferRepository simpleTransferRepository;
    @Autowired
    private RegularTransferRepository regularTransferRepository;
    @Autowired
    private DirectDebitTransferRepository directDebitTransferRepository;
    @Autowired
    private AccountService accountService;

    public Iterable<SavedTransaction> findAllSavedTransactions() {
        return savedTransferRepository.findAll();
    }
    public Iterable<SimpleTransaction> findAllSimpleTransactions() {
        return simpleTransferRepository.findAll();
    }
    public Iterable<RegularTransaction> findAllRegularTransactions() {
        return regularTransferRepository.findAll();
    }
    public Iterable<DirectDebitTransaction> findAllDirectDebitTransactions() { return directDebitTransferRepository.findAll(); }
    public SavedTransaction findSavedTransactionByComment(String c) {

        return savedTransferRepository.findByComment(c);
    }
    public Iterable<Transaction> findAllTransactions() { return transferRepository.findAll(); }

    public List<Transaction> findTransactionsByAccountNumber(String accountNumber) {
        List<Transaction> transactions = new ArrayList<>();
        for (Transaction transaction : transferRepository.findAll()) {
            if (transaction.getOwnAccountNumber().equals(accountNumber) || transaction.getTargetAccountNumber().equals(accountNumber)) {
                transactions.add(transaction);
            }
        }
        return transactions;
    }
    public SimpleTransaction doTransfer(TransactionView transactionView) {
        SimpleTransaction simpleTransaction = new SimpleTransaction();

        simpleTransaction.setAmount(transactionView.getAmount());
        simpleTransaction.setComment(transactionView.getComment());
        simpleTransaction.setDate(LocalDateTime.now());
        simpleTransaction.setOwnAccountNumber(transactionView.getOwnAccountNumber());
        simpleTransaction.setTargetAccountNumber(transactionView.getTargetAccountNumber());
        simpleTransaction.setType(transactionView.getType());

        Account ownAccount = accountService.findPersonalAccountByAccountNumber(transactionView.getOwnAccountNumber());
        Account targetAccount = accountService.findPersonalAccountByAccountNumber(transactionView.getTargetAccountNumber());

        int ownBalance = ownAccount.getBalance();
        int targetBalance = targetAccount.getBalance();
        ownBalance -= transactionView.getAmount();
        targetBalance += transactionView.getAmount();
        ownAccount.setBalance(ownBalance);
        targetAccount.setBalance(targetBalance);

        simpleTransferRepository.save(simpleTransaction);

        return simpleTransaction;
    }

    public SavedTransaction saveTransfer(TransactionView transactionView) {
        SavedTransaction savedTransaction = new SavedTransaction();

        savedTransaction.setAmount(transactionView.getAmount());
        savedTransaction.setComment(transactionView.getComment());
        savedTransaction.setDate(LocalDateTime.now());
        savedTransaction.setOwnAccountNumber(transactionView.getOwnAccountNumber());
        savedTransaction.setTargetAccountNumber(transactionView.getTargetAccountNumber());
        savedTransaction.setType("SAVED");

        savedTransferRepository.save(savedTransaction);

        return savedTransaction;
    }
    
}
