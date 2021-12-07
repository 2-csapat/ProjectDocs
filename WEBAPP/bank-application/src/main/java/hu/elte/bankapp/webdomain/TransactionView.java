package hu.elte.bankapp.webdomain;

import hu.elte.bankapp.configuration.ValidAccount;

import java.time.LocalDateTime;

public class TransactionView {
    private  String type;
    private  int amount;
    private  String comment;
    private  LocalDateTime date;
    private   String ownAccountNumber;
    @ValidAccount
    private   String targetAccountNumber;

    public TransactionView() {
    }


    public TransactionView(String type, int amount, String comment, LocalDateTime date, String ownAccountNumber, String targetedAccountNumber) {
        this.ownAccountNumber=ownAccountNumber;
        this.type = type;
        this.amount = amount;
        this.comment = comment;
        this.date = date;
        this.targetAccountNumber = targetedAccountNumber;
    }

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



    public void setOwnAccountNumber(String ownAccountNumber) {
        this.ownAccountNumber = ownAccountNumber;
    }

    public String getOwnAccountNumber() {
        return ownAccountNumber;
    }

    @Override
    public String toString() {
        return "TransactionView{" +
                "type='" + type + '\'' +
                ", amount=" + amount +
                ", comment='" + comment + '\'' +
                ", date=" + date +
                ", ownAccountNumber='" + ownAccountNumber + '\'' +
                ", targetedAccountNumber='" + targetAccountNumber + '\'' +
                '}';
    }

}
