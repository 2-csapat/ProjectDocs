package utils;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Transaction {
    private final SimpleIntegerProperty Sender;
    private final SimpleDoubleProperty Amount;
    private final SimpleStringProperty Currency;
    private final SimpleIntegerProperty Receiver;

    public Transaction(int sender, double amount, String currency, int receiver) {
        this.Sender = new SimpleIntegerProperty(sender);
        this.Amount = new SimpleDoubleProperty(amount);
        this.Currency = new SimpleStringProperty(currency);
        this.Receiver = new SimpleIntegerProperty(receiver);
    }

    public int getSender() {
        return Sender.get();
    }

    public SimpleIntegerProperty senderProperty() {
        return Sender;
    }

    public void setSender(int sender) {
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

    public int getReceiver() {
        return Receiver.get();
    }

    public SimpleIntegerProperty receiverProperty() {
        return Receiver;
    }

    public void setReceiver(int receiver) {
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
