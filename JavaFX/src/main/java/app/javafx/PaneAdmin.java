package app.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class PaneAdmin extends AnchorPane {
    public PaneAdmin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("adminMenu.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.load();
    }
}