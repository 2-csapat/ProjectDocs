package app.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;

import utils.Currency;
import utils.Customer;
import utils.Transaction;

/**
 * Controller for PaneTransaction
 */
public class PaneTransactionController {

    private Customer customer;
    private ArrayList<Currency> currencys;
    private boolean loaded = false;
    private final DataBaseServices dataBaseServices = DataBaseServices.getInstance();

    public PaneTransactionController() {

    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ArrayList<Currency> getCurrencys() {
        return currencys;
    }

    public void setCurrencys(ArrayList<Currency> currencys) {
        this.currencys = currencys;
    }

    @FXML
    ComboBox currency;

    @FXML
    void loadCurrency() {
        if (!loaded) {
            // turns enum to an observableList, so the choices can be set
            currencys = dataBaseServices.getCurrencys();

            ArrayList<String> currencyNames = new ArrayList<>();
            for(Currency c : currencys) {
                currencyNames.add(c.getName());
            }

            ObservableList<String> list = FXCollections.observableArrayList(currencyNames);
            currency.setItems(list);
            currency.setVisibleRowCount(5);
            loaded = true;

        }
    }

    @FXML
    TextField receiver;
    @FXML
    TextField amount;

    @FXML
    void processTransaction() {
        try {
            for (Currency c : currencys) {
                if (c.getName() == currency.getValue()) {
                    Transaction transaction = new Transaction(dataBaseServices.getAccountNumber(customer.getId()), Double.parseDouble(amount.getText()), (String) currency.getValue(), receiver.getText());
                    DataBaseServices dataBaseServices = DataBaseServices.getInstance();
                    dataBaseServices.processTransaction(transaction, c);
                    break;
                    }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }


        amount.setText("");
        receiver.setText("");
        currency.setValue(null);
    }
}
