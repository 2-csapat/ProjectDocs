package app.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.Vector;

import utils.Currency;
import utils.Customer;
import utils.MailUtil;
import utils.MyExceptions;
/**
 * Controller for MainApplication
 */
public class MainController {

    private Customer customer;
    private boolean loaded = false;
    private final DataBaseServices dataBaseServices = DataBaseServices.getInstance();
    private ArrayList<Currency> currencys;

    public MainController() { }

    @FXML
    VBox vBox;
    @FXML
    Button menu;
    @FXML
    Button deposit;
    @FXML
    Button withdraw;
    @FXML
    Button transaction;
    @FXML
    Button admin;
    @FXML
    Button adminTransaction;
    @FXML
    BorderPane borderPane;

    /**
     *
     * @param mouseEvent mainly used to declare where the user clicked
     * @throws IOException
     */
    @FXML
    public void loadPane(MouseEvent mouseEvent) throws IOException {
        // depending on mouse event, sets a pane to the borderpanes center

        if (mouseEvent.getSource() == menu) {
            PaneMenu paneMenu = new PaneMenu(customer.getFirstName(), customer.getLastName(), dataBaseServices.getAccountBalance(dataBaseServices.getAccountNumber(customer.getId())), dataBaseServices.getAccountNumber(customer.getId()));
            borderPane.setCenter(paneMenu);
        } else if (mouseEvent.getSource() == deposit) {
            PaneDeposit paneDeposit = new PaneDeposit(customer);
            borderPane.setCenter(paneDeposit);
        } else if (mouseEvent.getSource() == withdraw) {
            PaneWithdraw paneWithdraw = new PaneWithdraw(customer);
            borderPane.setCenter(paneWithdraw);
        } else if (mouseEvent.getSource() == transaction) {
            PaneTransaction paneTransaction = new PaneTransaction(customer, currencys);
            borderPane.setCenter(paneTransaction);
        } else if (mouseEvent.getSource() == admin) {
            PaneAdmin paneAdmin = new PaneAdmin();
            borderPane.setCenter(paneAdmin);
        } else if (mouseEvent.getSource() == adminTransaction) {
            PaneBankTransaction paneBankTransaction = new PaneBankTransaction();
            borderPane.setCenter(paneBankTransaction);
        }

    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @FXML
    Button buttonDeposit;
    @FXML
    TextField amountDeposit;
    @FXML
    ComboBox<String> currency;

    /**
     calls deposit if the requirements are met
     the deposit (textfield) amount is not empty, and it's a number (parseable to double)
     */
    public void deposit() {
        try {
            if (!amountDeposit.getText().isEmpty() && Double.parseDouble(amountDeposit.getText()) > 0 && currency.getValue()!=null) {
                for (Currency c : currencys) {
                    if (Objects.equals(c.getName(), this.currency.getValue())) {
                        dataBaseServices.updateAccBalance(dataBaseServices.getAccountNumber(customer.getId()), Double.parseDouble(amountDeposit.getText()) * (1/c.getValue()));
                        break;
                    }
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText("Sikeres befizet??s!");
                alert.setContentText("Befizet??se sikeresen megt??rt??nt.");
                alert.showAndWait();
                amountDeposit.setText("");
                currency.setValue(null);
            } else {
                throw new MyExceptions.InsufficientFunds();
            }
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("Sikertelen befizet??s!");
            alert.setContentText("K??rj??k gy??z??dj??n meg r??la, hogy a befizet??s adatai val??sak.");
            alert.showAndWait();
            amountDeposit.setText("");
            currency.setValue(null);
            exception.printStackTrace();
        }

    }

    @FXML
    Button buttonWithdraw;
    @FXML
    TextField amountWithdraw;
    /**
     calls withdraw if the requirements are met
     the withdraw (textfield) amount is not empty, and it's a number (parseable to double)
     */
    public void withdraw() {
        try {
            if (!amountWithdraw.getText().isEmpty() && Double.parseDouble(amountWithdraw.getText()) > 0 && currency.getValue() != null) {
                for (Currency c : currencys) {
                    if (Objects.equals(c.getName(), this.currency.getValue())) {
                        dataBaseServices.updateAccBalance(dataBaseServices.getAccountNumber(customer.getId()), Double.parseDouble(amountWithdraw.getText()) * (1 / c.getValue()) * -1);
                        break;
                    }
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText("Sikeres p??nzfelv??tel!");
                alert.setContentText("Sikeres kifizet??se megt??rt??nt.");
                alert.showAndWait();
                amountWithdraw.setText("");
                currency.setValue(null);
            } else {
                throw new MyExceptions.InsufficientFunds();
            }
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("Sikertelen befizet??s!");
            alert.setContentText("K??rj??k gy??z??dj??n meg r??la, hogy a befizet??s adatai val??sak.");
            alert.showAndWait();
            amountDeposit.setText("");
            currency.setValue(null);
            exception.printStackTrace();
        }
    }

    /**
     * Opens the project webpage
     * @throws URISyntaxException
     * @throws IOException
     */
    @FXML
    public void openWebpage() throws URISyntaxException, IOException {
        java.awt.Desktop.getDesktop().browse(new URI("http://bazsobeni.web.elte.hu/WEB4/index.html"));
    }

    /**
     * Opens the projects GitHub
     * @throws URISyntaxException
     * @throws IOException
     */
    @FXML
    public void openGitHub() throws URISyntaxException, IOException {
        java.awt.Desktop.getDesktop().browse(new URI("https://github.com/2-csapat/ProjectDocs"));
    }

    @FXML
    TextField subject;
    @FXML
    TextArea mailText;

    /**
     uses a gmail account, made for this purpose to send out emails to users email address'
     */
    @FXML
    public void sendMail() {
        try {

            Vector<String> mails;
            // getting email addresses into a list or array
            mails = dataBaseServices.getEmails();

            // sending email to the addresses
            MailUtil mailUtil = new MailUtil();
            for (String addressee : mails) {
                mailUtil.sendMail(addressee, subject.getText(), mailText.getText());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    @FXML
    Button adminExchangeRate;
    /**
     updates exchange rates through https://exchangeratesapi.io/
     the API return a json text, transforming it into String, then inserting into a mysql table
     */
    @FXML
    public void updateExchangeRates() {

        HttpURLConnection connection = null;
        try {
            // using stringbuilder to build data string
            String data;
            BufferedReader reader;
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            URL url = new URL("http://api.exchangeratesapi.io/v1/latest?access_key=0bc5260ed9b8c56d91e105ffcd9c3e3b");

            // setting connection properties
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();

            if (status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            data = stringBuilder.toString();
            // calling function that turns string into database table and inserting it into table
            dataBaseServices.updateExchangeRates(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        // to reduce the request 1 / login
        adminExchangeRate.setDisable(true);
        // feedback to admin, the data fetch was successful
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("V??lt??si ??rt??kek friss??t??se");
        alert.setHeaderText("V??lt??si adatok friss??t??se sikeresen megt??rt??nt");
        alert.setContentText("Folyamat sikeresen v??grehajtva, az adatok friss??t??se megt??rt??nt " + simpleDateFormat.format(timestamp));
        alert.showAndWait();
    }
    @FXML
    Button deleteAccount;
    @FXML
    void deleteAccount(MouseEvent mouseEvent) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Sz??mla t??rl??se.");
            alert.setHeaderText("??n jelenleg sz??ml??j??nak t??rl??s??re k??sz??l.");
            alert.setContentText("V??lasszon az al??bbiak k??z??l:");
            ButtonType buttonYes = new ButtonType("Igen");
            ButtonType buttonNo = new ButtonType("Nem");
            ButtonType buttonCancel = new ButtonType("M??gse", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonYes, buttonNo, buttonCancel);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonYes) {
                dataBaseServices.deleteAccount(customer.getId());
                Node node = (Node) mouseEvent.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.close();
                new LoginPage(mouseEvent);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * used to load currency's while the application is running
     */
    @FXML
    void loadCurrency() {
        if (!loaded) {
            // turns enum to an observableList, so the choices can be set
            currencys = dataBaseServices.getCurrencys();

            ArrayList<String> currencyNames = new ArrayList<>();
            for(Currency c : currencys) {
                currencyNames.add(c.getName());
            }

            ObservableList<String> list = FXCollections.observableArrayList(currencyNames);
            currency.setItems(list);
            currency.setVisibleRowCount(5);
            loaded = true;

        }
    }
}