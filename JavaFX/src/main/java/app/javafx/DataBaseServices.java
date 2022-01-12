package app.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.json.JSONObject;
import utils.Currency;
import utils.*;

import java.io.InputStream;
import java.security.SecureRandom;
import java.sql.*;
import java.util.*;

/**
 * A singleton object that used to communicate with the database.
 *
 */

public class DataBaseServices {

    private static final DataBaseServices instance = new DataBaseServices();

    private DataBaseServices() {
    }

    /**
     *  returns the singleton instance of the class
     */
    public static DataBaseServices getInstance() {
        return instance;
    }

    /**
     *  connects to database using the dbconfig.properties to fetch information
     */
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

    /**
     * completes a registration if it met the parameters
     * @param customer  an instance of Customer
     * @param password  the users password
     * @param accountType the type of the current account
     */
    void register(Customer customer, String password, AccountType accountType) {
        try {
            if (usedUsername(customer.getUsername()) && emailCheck(customer.getEmail())) {
                String userId = null;
                String accountId = null;
                String salt = generateSalt();
                SHA3_512 sha3_256 = new SHA3_512(password, salt);
                Connection connection = connect();
                try {
                    assert connection != null;
                    connection.setAutoCommit(false);
                    // Felhasználó hozzáadása
                    try (PreparedStatement addUser = connection.prepareStatement("Insert into Users(ID ,Username, FirstName, LastName, Password, Salt, Email, Phone_num, Birth_date) Values(?,?,?,?,?,?,?,?,?)")) {
                        UUID uuid;
                        ResultSet resultSet;
                        do {
                            uuid = UUID.randomUUID();
                            PreparedStatement ps = connection.prepareStatement("SELECT ID FROM users where ID = ?");
                            ps.setString(1, String.valueOf(uuid));
                            resultSet = ps.executeQuery();
                        } while (resultSet.next());
                        addUser.setString(1, String.valueOf(uuid));
                        addUser.setString(2, customer.getUsername());
                        addUser.setString(3, customer.getFirstName());
                        addUser.setString(4, customer.getLastName());
                        addUser.setString(5, sha3_256.encoder());
                        addUser.setString(6, salt);
                        addUser.setString(7, customer.getEmail());
                        addUser.setString(8, customer.getPhoneNum());
                        addUser.setString(9, customer.getBirthDate());
                        addUser.executeUpdate();
                        userId = uuid.toString();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    // Fiók hozzáadása
                    try (PreparedStatement addAccount = connection.prepareStatement("INSERT INTO accounts(ID, Type, Balance) VALUES (?,?,?)")) {
                        UUID uuid;
                        ResultSet resultSet;
                        do {
                            uuid = UUID.randomUUID();
                            PreparedStatement ps = connection.prepareStatement("SELECT ID FROM accounts WHERE ID = ?");
                            ps.setString(1, String.valueOf(uuid));
                            resultSet = ps.executeQuery();
                        } while (resultSet.next());
                        addAccount.setString(1, String.valueOf(uuid));
                        addAccount.setString(2, accountType.toString());
                        addAccount.setDouble(3, 0);
                        addAccount.executeUpdate();
                        accountId = uuid.toString();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    // Adatbázisbeli kapcsolat létrehozása
                    if (userId != null && accountId != null) {
                        try (PreparedStatement linkAccount = connection.prepareStatement("Insert into mappings(UsersID, AccountsID) Values (?,?)")) {
                            linkAccount.setString(1, userId); // linkAcc -> linkAccount
                            linkAccount.setString(2, accountId);
                            linkAccount.executeUpdate();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        connection.commit();
                    } else {
                        connection.rollback();
                    }
                    // kapcsolat bontása
                    connection.close();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * generates Salt for database
     * @return String of bytes
     */
    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        // dont change
        return bytes.toString();
    }

    /**
     *
     * @param username  given username
     * @param password  given password
     * @return null if login was not successful, else returns the registered id
     */
    public String login(String username, String password) {
        Connection connection = connect();
        ResultSet resultSet = null;
        try {
            assert connection != null;
            SHA3_512 sha3_512 = new SHA3_512(password, getSalt(username, connection));
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID FROM Users WHERE Username = ? AND Password = ?");
            preparedStatement.setString(1,username);
            preparedStatement.setString(2, sha3_512.encoder());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String id;
                id = resultSet.getString(1);
                return id;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                assert connection != null;
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                assert resultSet != null;
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     *
     * @param username      the name of the user
     * @param connection    Connection to database
     * @return the users Salt
     */
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

    /**
     *
     * @param id    the id to look for in the database
     * @return      the Current customer (type Customer)
     */
    Customer getUser(String id) {
        Customer customer = null;
        try {
            Connection connection = connect();
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT Username, FirstName, LastName, Email, Phone_num, Birth_date FROM Users WHERE ID = ?")) {
                preparedStatement.setString(1, id);
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

    /**
     *
     * @param id    the id to look for in the database
     * @return      returns AccountNumber(UUID) as a String
     */
    String getAccountNumber(String id) {
        try {
            Connection connection = connect();
            ResultSet resultSet = null;
            try {
                assert connection != null;
                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT Accounts.ID FROM accounts JOIN mappings m ON accounts.ID = m.AccountsID WHERE UsersID = ?")) {
                    preparedStatement.setString(1, id);
                    resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        return resultSet.getString(1);
                    }
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            } finally {
                assert resultSet != null;
                resultSet.close();
                connection.close();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    /**
     *
     * @param accountId     the Account that need to be changed
     * @param balanceChange the amount of the change
     */
    void updateAccBalance(String accountId, double balanceChange) {
        try {
            Connection connection = connect();

            // getting current balance
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Balance FROM Accounts WHERE ID = ?");
            preparedStatement.setString(1, accountId);
            ResultSet resultSet = preparedStatement.executeQuery();
            double currentBalance;
            resultSet.next();
            currentBalance = resultSet.getDouble("Balance");

            // updating balance
            if (currentBalance + balanceChange >= 0) {
                preparedStatement = connection.prepareStatement("Update Accounts Set Balance = ? Where ID = ?");
                preparedStatement.setDouble(1, currentBalance + balanceChange);
                preparedStatement.setString(2, accountId);
                preparedStatement.executeUpdate();
            } else {
                throw new MyExceptions.InsufficientFunds();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     *
     * @param accountId the accountID to look for in the database
     * @return returns the balance
     */
    double getAccountBalance(String accountId) {
        try {
            Connection connection = connect();

            // getting current balance
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Balance FROM Accounts WHERE ID = ?");
            preparedStatement.setString(1, accountId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getDouble("Balance");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    /**
     *
     * @param id the id that need to be deleted from database
     */
    void deleteAccount(String id) {
        Connection connection = connect();
        assert connection != null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT AccountsID FROM mappings where UsersID = ?");
            preparedStatement.setString(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int accountID = resultSet.getInt(1);
            preparedStatement = connection.prepareStatement("DELETE FROM mappings where UsersID = ?");
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("DELETE FROM users where ID = ?");
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("DELETE FROM accounts where ID = ?");
            preparedStatement.setInt(1, accountID);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Checks whether username is already taken
     * @param username
     * @return is false it its used
     */
    public boolean usedUsername(String username) {
        try {
            Connection connection = connect();
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Username FROM Users WHERE Username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            // van e ilyen nevű felhasználó
            if (resultSet.next()) {
                resultSet.close();
                connection.close();
                throw new MyExceptions.UsedUserName();
            }
            resultSet.close();
            connection.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return true;
    }

    /**
     * Checks whether email address is already taken
     * @param email
     * @return is false if its used
     */
    private boolean emailCheck(String email) {
        try {
            Connection connection = connect();
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Email FROM Users WHERE Email = ?");
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                resultSet.close();
                connection.close();
                throw new MyExceptions.UsedEmail();
            }
            resultSet.close();
            connection.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return true;
    }

    /**
     *
     * @param id the id that need to be checked
     * @return returns true if the user is an admin
     */
    private boolean isAdmin(String id) {
        try {
            Connection connection = connect();
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID FROM Admins WHERE UsersID = ?");
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean result = resultSet.next();
            resultSet.close();
            connection.close();
            return result;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @return returns currencys as an arraylist
     */
    public ArrayList<Currency> getCurrencys() {
        ArrayList<Currency> currencys = new ArrayList<>();
        try {
            Connection connection = connect();
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
            Connection connection = connect();
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Email FROM Users");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                mails.add(resultSet.getString(1));
            }
            connection.close();
            return mails;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public ObservableList<Transaction> getTransactions() {
        ArrayList<Transaction> transactionArrayList = new ArrayList<>();
        try {
            Connection connection = connect();
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT SenderID, Amount, Currency, ReceiverID FROM Transactions");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                transactionArrayList.add(new Transaction(resultSet.getString("SenderID"), resultSet.getDouble("Amount"), resultSet.getString("Currency"), resultSet.getString("ReceiverID")));
            }
            connection.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return FXCollections.observableArrayList(transactionArrayList);
    }

    /**
     *
     * @param transaction the transaction that needs to be processed
     * @param currency the currency of that transaction
     */
    public void processTransaction(Transaction transaction, Currency currency) {
        try {
            if (!Objects.equals(transaction.getSender(), transaction.getReceiver())) {
                Connection connection = connect();
                // if the receiver is a valid number
                assert connection != null;
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID FROM accounts WHERE ID = ?");
                preparedStatement.setString(1, transaction.getReceiver());
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    // check if the value is valid
                    preparedStatement = connection.prepareStatement("SELECT Balance FROM Accounts WHERE ID = ?");
                    preparedStatement.setString(1, transaction.getSender());
                    resultSet = preparedStatement.executeQuery();
                    int currentBalance = -1;
                    if (resultSet.next()) {
                        currentBalance = resultSet.getInt("Balance");
                    }
                    if (currentBalance - transaction.getAmount() >= 0) {
                        // transaction parameters met the requirements
                        // inserting transaction into table Transactions
                        preparedStatement = connection.prepareStatement("INSERT INTO Transactions(SenderID, Amount, Currency, ReceiverID) VALUES (?,?,?,?)");
                        preparedStatement.setString(1, transaction.getSender());
                        preparedStatement.setDouble(2, transaction.getAmount());
                        preparedStatement.setString(3, transaction.getCurrency());
                        preparedStatement.setString(4, transaction.getReceiver());
                        preparedStatement.executeUpdate();

                        updateAccBalance(transaction.getSender(), transaction.getAmount() * (1 / currency.getValue()) * -1);
                        updateAccBalance(transaction.getReceiver(), transaction.getAmount() * (1 / currency.getValue()) );
                    }
                    connection.close();
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

    /** updating exchange rates in database
     *
     * @param data incoming json object in string
     */
    public void updateExchangeRates(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject rates = jsonObject.getJSONObject("rates");
            // connecting to database
            Connection connection = connect();
            // checking if the database has table "ExchangeRates", if so delete everything from it
            assert connection != null;
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getTables(null, null, "ExchangeRates", null);
            if (rs.next()) {
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM ExchangeRates");
                preparedStatement.execute();
            }

            Iterator<String> keys = rates.keys();
            while (keys.hasNext()) {
                String currentDynamicKey = keys.next();
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ExchangeRates VALUES (?, ?)");
                preparedStatement.setString(1, currentDynamicKey);
                preparedStatement.setDouble(2, rates.getDouble(currentDynamicKey));
                preparedStatement.execute();
            }
            connection.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
