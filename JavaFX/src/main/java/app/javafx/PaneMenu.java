package app.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Pane for the menu
 */
public class PaneMenu extends AnchorPane {

    public PaneMenu(String firstname, String lastname, double balance, String cardNumber) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PaneMenu.class.getResource("menu.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.load();
        PaneMenuController paneMenuController = fxmlLoader.getController();
        paneMenuController.setName(firstname, lastname);
        paneMenuController.setBalance(balance);
        paneMenuController.setCardNumber(cardNumber);
        paneMenuController.startTimer();
    }
}
