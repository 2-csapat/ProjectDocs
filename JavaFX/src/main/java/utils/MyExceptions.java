package utils;

import javafx.scene.control.Alert;

public class MyExceptions {
    public static class UsedUserName extends Exception {

        public UsedUserName() {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Hiba");
            alert.setHeaderText("Hiba lépett fel a regisztráció során!");
            alert.setContentText("Használt felhasználónév! Kérjük adjon meg egy másik felhasználónevet.");
            alert.showAndWait();
        }
    }

    public static class BadUserName extends Exception {

        public BadUserName() {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Hiba");
            alert.setHeaderText("Hiba lépett fel a regisztráció során!");
            alert.setContentText("Nem megfelelő felhasználónév. ");
            alert.showAndWait();
        }
    }

    public static class UsedEmail extends Exception {

        public UsedEmail() {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Hiba");
            alert.setHeaderText("Hiba lépett fel a regisztráció során!");
            alert.setContentText("A megadott email cím már használt.");
            alert.showAndWait();
        }
    }

    public static class Short extends Exception {

        public Short() {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Hiba");
            alert.setHeaderText("Hiba lépett fel a regisztráció során!");
            alert.setContentText("A jelszónak minimum 12 karakternek kell lennie.");
            alert.showAndWait();
        }
    }

    public static class NoUpperc extends Exception {

        public NoUpperc() {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Hiba");
            alert.setHeaderText("Hiba lépett fel a regisztráció során!");
            alert.setContentText("A jelszónak tartalmaznia kell nagy betűt.");
            alert.showAndWait();
        }
    }

    public static class NoLowerC extends Exception {

        public NoLowerC() {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Hiba");
            alert.setHeaderText("Hiba lépett fel a regisztráció során!");
            alert.setContentText("A jelszónak tartalmaznia kell kis betűt.");
            alert.showAndWait();
        }
    }

    public static class NoNumber extends Exception {

        public NoNumber() {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Hiba");
            alert.setHeaderText("Hiba lépett fel a regisztráció során!");
            alert.setContentText("A jelszónak tartalmaznia kell számot.");
            alert.showAndWait();
        }
    }

    public static class Empty extends Exception {

        public Empty() {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Hiba");
            alert.setHeaderText("Hiba lépett fel a regisztráció során!");
            alert.setContentText("A jelszó mező nem lehet üres.");
            alert.showAndWait();
        }
    }

    public static class NoSpecial extends Exception {

        public NoSpecial() {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Hiba");
            alert.setHeaderText("Hiba lépett fel a regisztráció során!");
            alert.setContentText("A jelszónak tartalmaznia kell speciális karaktert (pl.: $ ß @ &). ");
            alert.showAndWait();
        }
    }

    public static class Space extends Exception {

        public Space() {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Hiba");
            alert.setHeaderText("Hiba lépett fel a regisztráció során!");
            alert.setContentText("A jelszó nem tartalmazhat szóközt. ");
            alert.showAndWait();
        }
    }

    public static class DiffPw extends Exception {

        public DiffPw() {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Hiba");
            alert.setHeaderText("Hiba lépett fel a regisztráció során!");
            alert.setContentText("A megadott jelszavak eltérnek. ");
            alert.showAndWait();
        }
    }

    public static class InvalidPhoneNum extends Exception {

        public InvalidPhoneNum() {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Hiba");
            alert.setHeaderText("Hiba lépett fel a regisztráció során!");
            alert.setContentText("A telefonszámot a következő minta alapján adja meg: +36/xxxxxxxxx. ");
            alert.showAndWait();
        }
    }

    public static class InvalidName extends Exception {
        public InvalidName() {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Hiba");
            alert.setHeaderText("Hiba lépett fel a regisztráció során!");
            alert.setContentText("A név nem teljesíti egy név alap feltételeit.");
            alert.showAndWait();
        }
    }

    public static class NoChoice extends Exception {
        public NoChoice() {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Hiba");
            alert.setHeaderText("Hiba lépett fel a regisztráció során!");
            alert.setContentText("Nem választott bankszámla típust.");
            alert.showAndWait();
        }
    }

    public static class InsufficientFunds extends Exception {
        public InsufficientFunds() {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Hiba");
            alert.setHeaderText("Hiba lépett fel az utalás során!");
            alert.setContentText("Nem megfelelő mennyiség.");
            alert.showAndWait();
        }
    }

    public static class InvalidDate extends Exception {
        public InvalidDate() {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Hiba");
            alert.setHeaderText("Hiba lépett fel a regisztráció során!");
            alert.setContentText("A regisztációhoz szükséges életkor: 18. ");
            alert.showAndWait();
        }
    }
}
