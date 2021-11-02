
package App;

public class Customer {
    /*
        
    */
    private final String username;
    private final String firstname;
    private final String lastname;
    private final String password;
    private final String email;
    private final String phoneNum;
    private final String birthDate;
    private final Account account;
    private final boolean admin;

    public Customer(String username, String firstName, String lastName, String password, String email, String phoneNum, String birthDate, Account account, boolean admin) {
        this.username = username;
        this.firstname = firstName;
        this.lastname = lastName;
        this.password = password;
        this.email = email;
        this.phoneNum = phoneNum;
        this.birthDate = birthDate;
        this.account = account;
        this.admin = admin;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public boolean isAdmin() {
        return admin;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getLastName() {
        return lastname;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public Account getAccount() {
        return account;
    }
    
    @Override
    public String toString() {
        return "Customer{" + "username=" + username + ", firstName=" + firstname + ", lastName=" + lastname + ", password=" + password + ", email=" + email + ", phoneNum=" + phoneNum + ", birthDate=" + birthDate + '}';
    }
    
}
