package hu.elte.bankapp.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DirectDebitTransaction{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String providerName;
    private String providerAccountNumber;
    private int amount;

    public DirectDebitTransaction(String providerName, String providerAccountNumber, int amount) {
        this.providerName = providerName;
        this.providerAccountNumber = providerAccountNumber;
        this.amount = amount;
    }

    public DirectDebitTransaction() { }

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
