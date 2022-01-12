package app.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import utils.Customer;

import java.io.IOException;

/**
 * Pane for withdraw
 */
public class PaneWithdraw extends AnchorPane {
    public PaneWithdraw(Customer customer) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("withdrawMenu.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();
        mainController.setCustomer(customer);
    }
}
