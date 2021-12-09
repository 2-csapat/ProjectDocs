package app.javafx;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaneMenuController {

    private String name;
    private double balance;
    private int cardNumber;

    @FXML
    Label cardOwnerName;
    @FXML
    Label cardmoney;
    @FXML
    Label cardNumberLabel;
    @FXML
    Label time;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        decimalFormat.setRoundingMode(RoundingMode.CEILING);
        cardmoney.setText("€ "+ decimalFormat.format(balance));
    }

    public String getName() {
        return name;
    }

    public void setName(String firstname, String lastname) {
        this.name = firstname + " " + lastname;
        cardOwnerName.setText(name);

    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
        cardNumberLabel.setText("Card number: " + this.cardNumber);
    }

    @Override
    public String toString() {
        return "PaneMenuController{" +
                "name='" + name + '\'' +
                ", balance=" + balance +
                '}';
    }

    void startTimer() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. MMM dd. HH:mm");
            time.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(5)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

}
