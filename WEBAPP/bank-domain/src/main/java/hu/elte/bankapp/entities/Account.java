package hu.elte.bankapp.entities;

import hu.elte.bankapp.models.Status;

import javax.persistence.*;

@Entity
@Table(name = "Account")
public class Account {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private int balance;
    private String accountNumber;
    private String constructionName;
    private Status status;

    @ManyToOne
    private User user;

    public int getId() {
        return id;
    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public void setId(int id) {
        this.id = id;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getConstructionName() {
        return constructionName;
    }

    public void setConstructionName(String constructionName) {
        this.constructionName = constructionName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }



    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                ", accountNumber='" + accountNumber + '\'' +
                ", constructionName='" + constructionName + '\'' +
                ", status=" + status +
                '}';
    }
}
