package app.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class RegistrationPage extends Application {

    public RegistrationPage(MouseEvent event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        start(stage);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(RegistrationPage.class.getResource("registration.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 540);
        primaryStage.getIcons().add(new Image("https://raw.githubusercontent.com/2-csapat/ProjectDocs/main/JavaFX/src/main/resources/images/registration.png"));
        primaryStage.setTitle("Regisztráció!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}
