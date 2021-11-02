
package App;

public abstract class Account {
    private double balance = 0;
    private int accountNumber = -1;

    public abstract AccountType getAccountType();
    
    Account(int accountNumber){
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getAccountnumber() {
        return accountNumber;
    }    
}
