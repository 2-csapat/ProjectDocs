package utils;

/**
 * Subclass of Account
 */
public class Betetszamla extends Account {
    public Betetszamla (int accountNumber ,double deposit) {
        super(accountNumber);
        this.setBalance(deposit);
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.Betetszamla;
    }
}
