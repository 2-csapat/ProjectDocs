package app.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;


public class PaneBankTransaction extends AnchorPane {
    public PaneBankTransaction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(PaneBankTransaction.class.getResource("bankTransactionMenu.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
