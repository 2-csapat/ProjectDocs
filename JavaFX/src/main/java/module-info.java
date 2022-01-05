module app.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.mail;
    requires java.desktop;
    requires mysql.connector.java;
    requires org.controlsfx.controls;
    requires org.json;

    opens app.javafx to javafx.fxml;
    exports app.javafx;
    exports utils;
    opens utils to javafx.fxml;
}