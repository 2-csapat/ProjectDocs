package hu.elte.bankapp.webdomain;

public class AccountView {
    private int balance;
    private String accountNumber;
    private String name;
    private int dailyTransferLimit;

    public AccountView(int balance, String accountNumber) {
        this.balance = balance;
        this.accountNumber = accountNumber;
    }

    public AccountView(int balance, String accountNumber, String name, int dailyTransferLimit) {
        this.balance = balance;
        this.accountNumber = accountNumber;
        this.name = name;
        this.dailyTransferLimit = dailyTransferLimit;
    }

    public AccountView(int balance, String accountNumber, String name) {
        this.balance = balance;
        this.accountNumber = accountNumber;
        this.name = name;
    }

    public AccountView() {

    }

    public int getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDailyTransferLimit() {
        return dailyTransferLimit;
    }

    public void setDailyTransferLimit(int dailyTransferLimit) {
        this.dailyTransferLimit = dailyTransferLimit;
    }
}
