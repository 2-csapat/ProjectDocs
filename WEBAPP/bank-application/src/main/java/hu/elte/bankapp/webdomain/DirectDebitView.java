package hu.elte.bankapp.webdomain;

public class DirectDebitView {
    private String providerName;
    private String providerAccountNumber;
    private int amount;

    public DirectDebitView(String providerName, String providerAccountNumber, int amount) {
        this.providerName = providerName;
        this.providerAccountNumber = providerAccountNumber;
        this.amount = amount;
    }

    public DirectDebitView() { }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderAccountNumber() {
        return providerAccountNumber;
    }

    public void setProviderAccountNumber(String providerAccountNumber) {
        this.providerAccountNumber = providerAccountNumber;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "DirectDebitView{" +
                "providerName='" + providerName + '\'' +
                ", providerAccountNumber='" + providerAccountNumber + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
