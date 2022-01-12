package app.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import utils.Currency;
import utils.Customer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Pane for transaction
 */
public class PaneTransaction extends AnchorPane {
    public PaneTransaction(Customer customer, ArrayList<Currency> currencys) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PaneTransaction.class.getResource("transactionMenu.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.load();
        PaneTransactionController paneTransactionController = fxmlLoader.getController();
        paneTransactionController.setCustomer(customer);
        paneTransactionController.setCurrencys(currencys);
    }
}
