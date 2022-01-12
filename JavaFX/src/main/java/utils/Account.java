package utils;

/**
 *
 * @author Simon Dominik Martin, Gazdag Aron, Boda Levente Miklos, Farkas Krisztian, Bazso Benjamin
 */

/**
 * Custom type for Accounts
 */
public abstract class Account {
    private double balance = 0;
    private int accountNumber = -1;

    /**
     *  Returns the type of the Account. It can be any AccountType.
     *
     */
    public abstract AccountType getAccountType();

    /**
     *  Constrcutor for Account with account number
     *
     */
    Account(int accountNumber){
        this.accountNumber = accountNumber;
    }

    /**
     *  Returns Account balance
     *
     */
    public double getBalance() {
        return balance;
    }

    /**
     *  Sets Account balance
     *
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     *  Returns Account number
     *
     */
    public int getAccountnumber() {
        return accountNumber;
    }
}
