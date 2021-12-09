package utils;

public class Folyoszamla extends Account {
    public Folyoszamla(int accountNumber, double deposit) {
        super(accountNumber);
        this.setBalance(deposit);
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.Folyoszamla;
    }
}
