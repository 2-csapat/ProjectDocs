package app.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import utils.*;

import java.io.InputStream;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;

/**
 * A singleton object that used to communicate with the database.
 *
 */

public class DataBaseServices {

    private static final DataBaseServices instance = new DataBaseServices();

    private final Connection connection = connect();

    private DataBaseServices() {
    }

    public static DataBaseServices getInstance() {
        return instance;
    }

    private Connection connect() {
        try {
            Properties properties = new Properties();
            Connection connection;
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("dbconfig.properties");
            properties.load(inputStream);

            String dbHost = properties.getProperty("DB_HOST");
            String dbName = properties.getProperty("DB_NAME");
            String dbUser = properties.getProperty("DB_USER");
            String dbPwd = properties.getProperty("DB_PWD");

            connection = DriverManager.getConnection(dbHost + "/" + dbName, dbUser, dbPwd);

            assert inputStream != null;
            inputStream.close();
            return connection;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }


    void register(Customer customer, String password, AccountType accountType) {
        try {
            if (usedUsername(customer.getUsername()) && emailCheck(customer.getEmail())) {
                int userId = -1;
                int accountId = -1;
                String salt = generateSalt();
                SHA3_512 sha3_256 = new SHA3_512(password, salt);
                try {
                    assert connection != null;
                    connection.setAutoCommit(false);
                    generateSalt();
                    // add user
                    try (PreparedStatement addUser = connection.prepareStatement("Insert into Users(Username, FirstName, LastName, Password, Salt, Email, Phone_num, Birth_date) Values(?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
                        addUser.setString(1, customer.getUsername());
                        addUser.setString(2, customer.getFirstName());
                        addUser.setString(3, customer.getLastName());
                        addUser.setString(4, sha3_256.encoder());
                        addUser.setString(5, salt);
                        addUser.setString(6, customer.getEmail());
                        addUser.setString(7, customer.getPhoneNum());
                        addUser.setString(8, customer.getBirthDate());
                        addUser.executeUpdate();
                        ResultSet resultSet = addUser.getGeneratedKeys();
                        if (resultSet.next()) {
                            userId = resultSet.getInt(1);
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    // add account
                    try (PreparedStatement addAccount = connection.prepareStatement("Insert into Accounts(Type, Balance) Values (?,?)", Statement.RETURN_GENERATED_KEYS)) {
                        addAccount.setString(1, accountType.toString());
                        addAccount.setDouble(2, 0);
                        addAccount.executeUpdate();
                        ResultSet resultSet = addAccount.getGeneratedKeys(); // rs -> resultSet
                        if (resultSet.next()) {
                            accountId = resultSet.getInt(1);
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    // connect to database
                    if (userId > 0 && accountId > 0) {
                        try (PreparedStatement linkAccount = connection.prepareStatement("Insert into mappings(UsersID, AccountsID) Values (?,?)")) {
                            linkAccount.setInt(1, userId); // linkAcc -> linkAccount
                            linkAccount.setInt(2, accountId);
                            linkAccount.executeUpdate();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        connection.commit();
                    } else {
                        connection.rollback();
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        // it works, do not touch it!
        return bytes.toString();
    }

    // checks whether registered user exists, if not, return -1
    public int login(String username, String password) {
        int id = -1;
        ResultSet resultSet = null;
        try {
            assert connection != null;
            SHA3_512 sha3_512 = new SHA3_512(password, getSalt(username, connection));
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID FROM Users WHERE Username = ? AND Password = ?");
            preparedStatement.setString(1,username);
            preparedStatement.setString(2, sha3_512.encoder());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
                return id;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                assert resultSet != null;
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    private String getSalt(String username, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Salt FROM Users WHERE Username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return "";
    }

    // Read
    Customer getUser(int id) {
        Customer customer = null;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT Username, FirstName, LastName, Email, Phone_num, Birth_date FROM Users WHERE ID = ?")) {
                preparedStatement.setInt(1, id);
                ResultSet result = preparedStatement.executeQuery();
                if (result.next()) {
                    String username = result.getString("Username");
                    String firstName = result.getString("FirstName");
                    String lastName = result.getString("LastName");
                    String email = result.getString("Email");
                    String phoneNum = result.getString("Phone_num");
                    String birthDate = result.getString("Birth_date");
                    customer = new Customer(id, username, firstName, lastName, email, phoneNum, birthDate, isAdmin(id));
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return customer;
    }

    int getAccountNumber(int id) {
        try {
            ResultSet resultSet = null;
            try {
                assert connection != null;
                try (PreparedStatement preparedStatement = connection.prepareStatement("select Accounts.ID from accounts join mappings m on accounts.ID = m.AccountsID where UsersID = ?")) {
                    preparedStatement.setInt(1, id);
                    resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            } finally {
                assert resultSet != null;
                resultSet.close();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return -1;
    }

    // Update (money in/out)
    void updateAccBalance(int accountId, double balanceChange) {
        try {
            // getting current balance
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Balance FROM Accounts WHERE ID = ?");
            preparedStatement.setInt(1, accountId);
            ResultSet resultSet = preparedStatement.executeQuery();
            double currentBalance;
            resultSet.next();
            currentBalance = resultSet.getDouble("Balance");

            // updating balance
            if (currentBalance + balanceChange >= 0) {
                preparedStatement = connection.prepareStatement("Update Accounts Set Balance = ? Where ID = ?");
                preparedStatement.setDouble(1, currentBalance + balanceChange);
                preparedStatement.setInt(2, accountId);
                preparedStatement.executeUpdate();
            } else {
                throw new MyExceptions.InsufficientFunds();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // Update (money in/out)
    double getAccBalance(int accountId) {
        try {
            // getting current balance
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Balance FROM Accounts WHERE ID = ?");
            preparedStatement.setInt(1, accountId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getDouble("Balance");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }


    // Delete
    void deleteAccount(int id) {
        assert connection != null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT AccountsID FROM mappings where UsersID = ?");
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int accountID = resultSet.getInt(1);
            System.out.println(accountID);
            preparedStatement = connection.prepareStatement("DELETE FROM mappings where UsersID = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("DELETE FROM users where ID = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("DELETE FROM accounts where ID = ?");
            preparedStatement.setInt(1, accountID);
            preparedStatement.executeUpdate();
            resultSet.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // other
    // checks whether username exists
    public boolean usedUsername(String username) {
        try {
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Username FROM Users WHERE Username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            // checks whether user exists
            if (resultSet.next()) {
                resultSet.close();
                throw new MyExceptions.UsedUserName();
            }
            resultSet.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return true;
    }

    // checks whether email exists
    private boolean emailCheck(String email) {
        try {
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Email FROM Users WHERE Email = ?");
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                resultSet.close();
                throw new MyExceptions.UsedEmail();
            }
            resultSet.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return true;
    }

    // checks whether current user is admin or not
    private boolean isAdmin(int id) {
        try {
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID FROM Admins WHERE UsersID = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean result = resultSet.next();
            resultSet.close();
            return result;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public ArrayList<Currency> getCurrencys() {
        ArrayList<Currency> currencys = new ArrayList<>();
        try {
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT currency, value FROM exchangerates");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                currencys.add(new Currency(resultSet.getString("currency"), resultSet.getDouble("value")));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return currencys;
    }

    public Vector<String> getEmails() {
        try {
            Vector<String> mails = new Vector<>();
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Email FROM Users");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                mails.add(resultSet.getString(1));
            }
            return mails;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public ObservableList getTransactions() {
        ArrayList<Transaction> transactionArrayList = new ArrayList<>();
        try {
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT SenderID, Amount, Currency, ReceiverID FROM Transactions");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                transactionArrayList.add(new Transaction(resultSet.getInt("SenderID"), resultSet.getDouble("Amount"), resultSet.getString("Currency"), resultSet.getInt("ReceiverID")));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return FXCollections.observableArrayList(transactionArrayList);
    }

    public void processTransaction(Transaction transaction, Currency currency) {
        try {
            if (transaction.getSender() != transaction.getReceiver()) {
                // checks if the receiver is a valid number
                assert connection != null;
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID FROM Users WHERE ID = ?");
                preparedStatement.setInt(1, transaction.getReceiver());
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    // checks if the value is valid
                    preparedStatement = connection.prepareStatement("SELECT Balance FROM Accounts WHERE ID = ?");
                    preparedStatement.setInt(1, transaction.getSender());
                    resultSet = preparedStatement.executeQuery();
                    int currentBalance = -1;
                    if (resultSet.next()) {
                        currentBalance = resultSet.getInt("Balance");
                    }
                    if (currentBalance - transaction.getAmount() >= 0) {
                        // transaction parameters met the requirements
                        // inserting transaction into table Transactions
                        preparedStatement = connection.prepareStatement("INSERT INTO Transactions(SenderID, Amount, Currency, ReceiverID) VALUES (?,?,?,?)");
                        preparedStatement.setInt(1, transaction.getSender());
                        preparedStatement.setDouble(2, transaction.getAmount());
                        preparedStatement.setString(3, transaction.getCurrency());
                        preparedStatement.setInt(4, transaction.getReceiver());
                        preparedStatement.executeUpdate();

                        updateAccBalance(transaction.getSender(), transaction.getAmount() * (1 / currency.getValue()) * -1);
                        updateAccBalance(transaction.getReceiver(), transaction.getAmount() * (1 / currency.getValue()) );
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sikeres tranzakció.");
                    alert.setHeaderText("Utalását sikeresen elindította.");
                    alert.setContentText("Köszönjük hogy bankunkat választotta.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Hiba lépett fel!");
                    alert.setHeaderText("Hiba a tranzakciós adatokban.");
                    alert.setContentText("Kérjük ellenőrizze a tranzakciós adatokat, és próbálkozzon újra.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Hiba lépett fel!");
                alert.setHeaderText("Hiba a tranzakciós adatokban.");
                alert.setContentText("Kérjük ellenőrizze a tranzakciós adatokat, és próbálkozzon újra.");
                alert.showAndWait();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // exchange rates to db
    public void updateExchangeRates(String data) {
        try {
            /**
                the string has a fix start if the request is successful: {"success
                the first country is AED - the preceding text is unnecessary
                replacing brackets
             */
            int startIndex = data.indexOf("{\"success");
            int endIndex = data.lastIndexOf("\"AED");
            String replacement = "";
            String toBeReplaced = data.substring(startIndex + 1, endIndex);
            data = data.replace(toBeReplaced, replacement).replace("{", "").replace("}", "");
            // splitting text by ',' -> one line: "AED":4.244
            String[] temp = data.split(",");
            String[] dataSet;
            // connecting to database
            // checking if the database has table "ExchangeRates", if so delete everything from it
            assert connection != null;
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getTables(null, null, "ExchangeRates" , null);
            if (rs.next()) {
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM ExchangeRates");
                preparedStatement.execute();
            }
            // replacing "\"" with nothing ,splitting text by ":", and inserting data line by line
            for (String string : temp) {
                dataSet = string.replace("\"", "").split(":");
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ExchangeRates VALUES (?, ?)");
                preparedStatement.setString(1, dataSet[0]);
                preparedStatement.setString(2, dataSet[1]);
                preparedStatement.execute();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
