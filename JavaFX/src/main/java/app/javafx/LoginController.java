package app.javafx;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.Customer;

public class LoginController {
    @FXML
    Button btnSignin;
    @FXML
    Button btnReg;
    @FXML
    TextField loginName;
    @FXML
    PasswordField loginPassword;

    /**
     * starts the login progress
     * @param event used to declare where the user clicked
     */
    @FXML
    protected void handleButtonAction(MouseEvent event) {
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();

            if (event.getSource() == btnSignin) {
                // get database
                DataBaseServices dbServices = DataBaseServices.getInstance();
                if (dbServices.login(loginName.getText(), loginPassword.getText()) != null) {
                    //login successful
                    Customer customer = dbServices.getUser(dbServices.login(loginName.getText(), loginPassword.getText()));
                    stage.close();
                    new MainApplication(event, customer);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    alert.setHeaderText("Helytelen felhasználónév vagy jelszó!");
                    alert.setContentText("Ellenőrizze, hogy helyes felhasználónevet vagy jelszót adott e meg!");
                    alert.showAndWait();
                }
            } else if (event.getSource() == btnReg) {
                stage.close();
                new RegistrationPage(event);
            }
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("Csatlakozás sikertelen");
            alert.setContentText("Kérjük ellenőrizze hogy rendelkezik-e internet kapcsolattal!");
            alert.showAndWait();
        }
    }
}
