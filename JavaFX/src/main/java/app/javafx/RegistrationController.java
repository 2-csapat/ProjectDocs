package app.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.AccountType;
import utils.Customer;
import utils.MyExceptions;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationController {

    // felhasználónév ellenőrzés illetve hogy használt-e
    private boolean userNameCheck(String username) throws Exception {
        if (!username.matches("^\\S{4,20}")) {
            throw new MyExceptions.BadUserName();
        }
        return true;
    }

    // megnézi a jelszó erősségét
    private boolean pwCheck(String pw, String pw2) throws Exception {
        // megnézi hogy a jelszó hosszabb-e mint 11 karakter
        if (pw.length() < 12) {
            throw new MyExceptions.Short();
        }
        // megnézi hogy a két jelszó mező tartalma megegyezik-e
        if (!pw.equals(pw2)) {
            throw new MyExceptions.DiffPw();
        }
        // megnézi hogy a jelszó tartalmaz-e speciális karaktert
        // regex jelölések: https://www.vogella.com/tutorials/JavaRegularExpressions/article.html
        // regex online tester: https://www.freeformatter.com/java-regex-tester.html
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(pw);
        boolean hasSpecial = matcher.find();
        if (!hasSpecial) {
            throw new MyExceptions.NoSpecial();
        }

        //  megnézi hogy a jelszó tartalmaz-e nagybetűt
        boolean hasUpperCase = false;
        for (int i = 0; i < pw.length(); i++) {
            if (Character.isUpperCase(pw.charAt(i))) {
                hasUpperCase = true;
            }
        }
        if (!hasUpperCase) {
            throw new MyExceptions.NoUpperc();
        }
        // megnézi hogy a jelszó tartalmaz-e kisbetűt
        boolean hasLowerCase = false;
        for (int i = 0; i < pw.length(); i++) {
            if (Character.isLowerCase(pw.charAt(i))) {
                hasLowerCase = true;
            }
        }
        if (!hasLowerCase) {
            throw new MyExceptions.NoLowerC();
        }
        // megnézi hogy a jelszó tartalmaz-e számot
        boolean hasNumber = false;
        for (int i = 0; i < pw.length(); i++) {
            if (Character.isDigit(pw.charAt(i))) {
                hasNumber = true;
            }
        }
        if (!hasNumber) {
            throw new MyExceptions.NoNumber();
        }
        // megnézi hogy a jelszó tartalmaz-e szóközt
        boolean hasWhiteSpace = false;
        for (int i = 0; i < pw.length(); i++) {
            if (Character.isSpaceChar(pw.charAt(i))) {
                hasWhiteSpace = true;
            }
        }
        if (hasWhiteSpace) {
            throw new MyExceptions.Space();
        }
        // igazat ad vissza a jelszó megfelelő
        return true;
    }

    // email valóságnak megfelelő-e (javax.mail)
    private boolean emailCheck(String mail) {
        boolean result = true;
        try {
            InternetAddress email = new InternetAddress(mail);
            email.validate();
        } catch (AddressException ex) {
            result = false;
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Hiba");
            alert.setHeaderText("Hiba lépett fel a regisztráció során.");
            alert.setContentText("Nem megfelelő email cím. ");
            alert.showAndWait();
        }
        return result;
    }

    // telefonszám ellenőrzése
    private boolean phoneNumCheck(String phoneNum) throws Exception {
        // regex jelölések: https://www.vogella.com/tutorials/JavaRegularExpressions/article.html
        // regex online tester: https://www.freeformatter.com/java-regex-tester.html
        Pattern pattern = Pattern.compile("^[+]\\d{2}[/]\\d{9}");
        Matcher matcher = pattern.matcher(phoneNum);
        if (matcher.find()) {
            return true;
        } else {
            throw new MyExceptions.InvalidPhoneNum();
        }
    }

    // név ellenőrzés
    private boolean nameCheck(String firstName, String lastName) throws Exception {
        if (!firstName.matches("^[A-Z]\\D{1,20}")) {
            throw new MyExceptions.InvalidName();
        }
        if (!lastName.matches("^[A-Z]\\D{1,20}")) {
            throw new MyExceptions.InvalidName();
        }
        return true;
    }

    // dátum ellenőrzése
    private boolean dateCheck(LocalDate date) throws Exception {
        if (date == null) throw new MyExceptions.InvalidDate();
        long epochdate = date.toEpochDay() * 86400000;
        long epochtoday = System.currentTimeMillis();
        // 18 years in millis = 568024668
        if (epochtoday - epochdate < 568024668) throw new MyExceptions.InvalidDate();
        return true;
    }

    // kiválasztották-e a bankszámlatípust a regisztráció során
    public boolean choiceBoxCheck(Object selectedItem) throws Exception {
        if (selectedItem == null) throw new MyExceptions.NoChoice();
        return true;
    }

    boolean loaded = false;

    @FXML
    TextField username;
    @FXML
    PasswordField password;
    @FXML
    PasswordField password2;
    @FXML
    TextField firstName;
    @FXML
    TextField lastName;
    @FXML
    TextField email;
    @FXML
    TextField phoneNum;
    @FXML
    DatePicker datePicker;
    @FXML
    ChoiceBox<String> choiceBox;
    @FXML
    Button buttonRegistration;
    @FXML
    Button buttonToLogin;

    public void load() {
        if (!loaded) {
            // turns enum to an observableList, so the choices can be set
            ObservableList<String> list = FXCollections.observableArrayList(Arrays.stream(AccountType.values()).map(Enum::toString).toArray(String[]::new));
            choiceBox.setItems(list);
            loaded = true;
        }
    }

    public void registration(MouseEvent event) {
        try {
            if (event.getSource() == buttonRegistration) {
                Customer customer = new Customer(null, username.getText(), firstName.getText(), lastName.getText(), email.getText(), phoneNum.getText(), datePicker.getValue().toString(), false);
                // if tests passed
                if (userNameCheck(customer.getUsername()) && pwCheck(password.getText(), password2.getText()) && dateCheck(datePicker.getValue())
                        && emailCheck(customer.getEmail()) && phoneNumCheck(customer.getPhoneNum()) && nameCheck(customer.getFirstName(), customer.getLastName())
                        && choiceBoxCheck(choiceBox.getSelectionModel().getSelectedItem())) {
                    // getting account type
                    AccountType accountType = AccountType.Folyoszamla;
                    if (choiceBox.getSelectionModel().getSelectedItem().equals("Betetszamla")) {
                        accountType = AccountType.Betetszamla;
                    }

                    // pushing into database
                    DataBaseServices dataBaseServices = DataBaseServices.getInstance();
                    dataBaseServices.register(customer, password.getText(), accountType);

                    // Information alert if registration successful
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("Registration successful!");
                    alert.setContentText("Your account is ready, you can log in.");
                    alert.showAndWait();

                    // closing registration scene and opening a new LoginPage
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    stage.close();
                    new LoginPage(event);
                }
            } else if (event.getSource() == buttonToLogin) {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.close();
                new LoginPage(event);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
