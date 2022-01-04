package utils;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Transaction {
    private final SimpleStringProperty Sender;
    private final SimpleDoubleProperty Amount;
    private final SimpleStringProperty Currency;
    private final SimpleStringProperty Receiver;

    public Transaction(String senderAccountID, double amount, String currency, String receiverAccountID) {
        this.Sender = new SimpleStringProperty(senderAccountID);
        this.Amount = new SimpleDoubleProperty(amount);
        this.Currency = new SimpleStringProperty(currency);
        this.Receiver = new SimpleStringProperty(receiverAccountID);
    }

    public String getSender() {
        return Sender.get();
    }

    public SimpleStringProperty senderProperty() {
        return Sender;
    }

    public void setSender(String sender) {
        this.Sender.set(sender);
    }

    public double getAmount() {
        return Amount.get();
    }

    public SimpleDoubleProperty amountProperty() {
        return Amount;
    }

    public String getCurrency() {
        return Currency.get();
    }

    public SimpleStringProperty currencyProperty() {
        return Currency;
    }

    public void setCurrency(String currency) {
        this.Currency.set(currency);
    }

    public void setAmount(int amount) {
        this.Amount.set(amount);
    }

    public String getReceiver() {
        return Receiver.get();
    }

    public SimpleStringProperty receiverProperty() {
        return Receiver;
    }

    public void setReceiver(String receiver) {
        this.Receiver.set(receiver);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "Sender=" + Sender +
                ", Amount=" + Amount +
                ", Receiver=" + Receiver +
                '}';
    }
}
