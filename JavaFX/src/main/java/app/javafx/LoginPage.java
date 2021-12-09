package app.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginPage extends Application {
    public LoginPage() { }

    public LoginPage(MouseEvent event) throws IOException, ClassNotFoundException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        start(stage);
    }

    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.getIcons().add(new Image("C:\\Users\\bazso\\3D Objects\\projektek\\soos\\ProjectDocs\\JavaFX\\target\\classes\\images\\login.png"));
        stage.setTitle("Bejelentkezés");
        stage.setScene(scene);
        stage.show();
        Class.forName("com.mysql.cj.jdbc.Driver");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
