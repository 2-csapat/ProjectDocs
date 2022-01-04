package app.javafx;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import utils.Transaction;


public class PaneBankTransactionController {
    @FXML
    TableView tableView;
    @FXML
    Button list;

    public PaneBankTransactionController() {

    }

    public void load() {
        tableView.getColumns().removeAll("Sender", "Amount", "Currency", "Receiver");
        DataBaseServices dbServices = DataBaseServices.getInstance();
        ObservableList<Transaction> data = dbServices.getTransactions();
        TableColumn<Transaction, Integer> sender = new TableColumn<>("Sender");
        TableColumn<Transaction, Double> amount = new TableColumn<>("Amount");
        TableColumn<Transaction, String> currency = new TableColumn<>("Currency");
        TableColumn<Transaction, Integer> receiver = new TableColumn<>("Receiver");
        sender.setMinWidth(120);
        amount.setMinWidth(120);
        currency.setMinWidth(120);
        receiver.setMinWidth(120);
        sender.setCellValueFactory(new PropertyValueFactory<>("sender"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        currency.setCellValueFactory(new PropertyValueFactory<>("currency"));
        receiver.setCellValueFactory(new PropertyValueFactory<>("receiver"));
        tableView.setItems(data);
        tableView.getColumns().addAll(sender, amount, currency, receiver);
        list.setDisable(true);
    }
}


