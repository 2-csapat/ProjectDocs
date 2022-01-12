package app.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import utils.Customer;

import java.io.IOException;

/**
 * Pane for the deposit panel
 */
public class PaneDeposit extends AnchorPane {
    public PaneDeposit(Customer customer) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("depositMenu.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();
        mainController.setCustomer(customer);
    }
}
