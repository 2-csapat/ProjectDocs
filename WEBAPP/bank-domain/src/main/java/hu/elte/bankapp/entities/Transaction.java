package hu.elte.bankapp.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String type;
    private int amount;
    private String comment;
    private LocalDateTime date;
    private String ownAccountNumber;
    private String targetAccountNumber;

    public Transaction() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwnAccountNumber() { return ownAccountNumber; }

    public void setOwnAccountNumber(String ownAccountNumber) { this.ownAccountNumber = ownAccountNumber; }

    public String getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setTargetAccountNumber(String targetAccountNumber) {
        this.targetAccountNumber = targetAccountNumber;
    }
}


