package app.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.Customer;


public class MainApplication extends Application {

    private final Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public MainApplication(MouseEvent event, Customer customer) {
        this.customer = customer;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        start(stage);
    }

    /**
     * loads the stage of the application depending on if the user is admin or not
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader;
            if (!customer.isAdmin()) {
                fxmlLoader = new FXMLLoader(MainApplication.class.getResource("viewUser.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 720, 540);
                MainController mainController = fxmlLoader.getController();
                mainController.setCustomer(customer);
                stage.setTitle("IBANK");
                stage.setScene(scene);
            } else{
                fxmlLoader = new FXMLLoader(MainApplication.class.getResource("viewAdmin.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 720, 540);
                MainController mainController = fxmlLoader.getController();
                mainController.setCustomer(customer);
                stage.setTitle("IBANK");
                stage.setScene(scene);
            }
            stage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }


    }

    public static void main(String[] args) {
        launch();
    }

}