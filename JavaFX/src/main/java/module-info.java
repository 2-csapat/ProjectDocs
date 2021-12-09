module app.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;
    requires java.mail;
    requires java.desktop;

    opens app.javafx to javafx.fxml;
    exports app.javafx;
    exports utils;
    opens utils to javafx.fxml;
}