package app.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Loginpage for the user. This is the starting point of our application.
 */
public class LoginPage extends Application {
    public LoginPage() { }

    public LoginPage(MouseEvent event) throws IOException, ClassNotFoundException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        start(stage);
    }

    /**
     * The main function that starts the program
     */
    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.getIcons().add(new Image("https://raw.githubusercontent.com/2-csapat/ProjectDocs/main/JavaFX/src/main/resources/images/login.png"));
        stage.setTitle("Bejelentkezés");
        stage.setScene(scene);
        stage.show();
        Class.forName("com.mysql.cj.jdbc.Driver");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
