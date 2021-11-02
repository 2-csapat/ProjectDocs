package App;

import java.sql.*;

public class DbServices {

    String url = "jdbc:mysql://localhost:3306/bankdb";
    String user = "bank";
    String password = "securepassword";

    private Connection connect() {
        Connection con;
        try {
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            con = null;
            System.err.println(e);
        }
        return con;
    }

    int register(String username, String firstName, String lastName, String password, String email, String phoneNum, String birthDate, double balance, AccountType accountType) throws Exception {
        if (usedUsername(username) && emailCheck(email)) {
            int userId = -1;
            int accountId = -1;
            Connection connection = connect();
            try {
                connection.setAutoCommit(false);
                // Felhasználó hozzáadása
                try ( PreparedStatement addUser = connection.prepareStatement("Insert into Users(Username, FirstName, LastName, Password, Email, Phone_num, Birth_date) Values(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
                    addUser.setString(1, username);
                    addUser.setString(2, firstName);
                    addUser.setString(3, lastName);
                    addUser.setString(4, password);
                    addUser.setString(5, email);
                    addUser.setString(6, phoneNum);
                    addUser.setString(7, birthDate);
                    addUser.executeUpdate();
                    ResultSet rs = addUser.getGeneratedKeys();
                    if (rs.next()) {
                        userId = rs.getInt(1);
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                // Fiók hozzáadása
                try ( PreparedStatement addAccount = connection.prepareStatement("Insert into Accounts(Type, Balance) Values (?,?)", Statement.RETURN_GENERATED_KEYS)) {
                    addAccount.setString(1, accountType.toString());
                    addAccount.setDouble(2, balance);
                    addAccount.executeUpdate();
                    ResultSet rs = addAccount.getGeneratedKeys();
                    if (rs.next()) {
                        accountId = rs.getInt(1);
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                // Adatbázisbeli kapcsolat létrehozása
                if (userId > 0 && accountId > 0) {
                    try ( PreparedStatement linkAcc = connection.prepareStatement("Insert into mappings(UsersID, AccountsID) Values (?,?)")) {
                        linkAcc.setInt(1, userId);
                        linkAcc.setInt(2, accountId);
                        linkAcc.executeUpdate();
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                    connection.commit();
                } else {
                    connection.rollback();
                }
                // kapcsolat bontása
                connection.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return 0;
    }

    // Read
    Customer getAcc(int id) {
        Customer customer = null;
        Connection connection = connect();
        try (PreparedStatement ps = connection.prepareStatement("SELECT Username, FirstName, Password, LastName, Email, Phone_num, Birth_date, Type, Balance FROM Users a JOIN Mappings b on a.ID = b.UsersId JOIN Accounts c on b.AccountsId = c.ID WHERE c.ID = ?", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                String username = result.getString("Username");
                String firstName = result.getString("FirstName");
                String lastName = result.getString("LastName");
                String pw = result.getString("Password");
                String email = result.getString("Email");
                String phoneNum = result.getString("Phone_num");
                String birthDate = result.getString("Birth_date");
                String accountType = result.getString("Type");
                Double balance = result.getDouble("Balance");
                Account account;
                if (accountType.equals(AccountType.Betétszámla.name())) {
                    account = new Betetszamla(id, balance);
                } else {
                    account = new Folyoszamla(id, balance);
                }
                customer = new Customer(username, firstName, lastName, pw, email, phoneNum, birthDate, account, isAdmin(id));
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return customer;
    }

    // Update (pénz be-ki)
    boolean updateAccBalance(int accountId, double balanceChange) {
        return true;
    }

    // Delete
    boolean deleteAcc(int id) {

        return true;
    }
    // other
    // megnézi használt-e a felhasználó név
    public boolean usedUsername(String username) throws Exception {
        Connection con = connect();
        String query = "SELECT Username FROM `Users` WHERE Username = '" + username + "'";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        // van e ilyen nevű felhasználó
        if (rs.next()) {
            rs.close();
            con.close();
            throw new MyExceptions.UsedUserName();
        }
        rs.close();
        con.close();
        return true;
    }

    // megnézi használt-e az email cím
    private boolean emailCheck(String email) throws Exception {
        Connection con = connect();
        String query = "SELECT Email FROM `Users` WHERE Email = '" + email + "'";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            rs.close();
            con.close();
            throw new MyExceptions.UsedEmail();
        }
        rs.close();
        con.close();
        return true;
    }

    // Megnézi hogy a van-e ilyen regisztrált felhasználó a bejelentkezéshez
    public int login(String username, String password) throws SQLException  {
        String query = "SELECT ID FROM Users WHERE Username = '" + username + "' AND Password = '" + password + "'";
        int id = -1;
        Connection con = connect();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            id = rs.getInt(1);
            rs.close();
            con.close();
            return id;
        }
        rs.close();
        con.close();
        return id;
    }

    private boolean isAdmin(int id) throws SQLException {
        Connection connection = connect();
        PreparedStatement ps = connection.prepareStatement("SELECT ID FROM Admins WHERE UsersID = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

}
