package utils;
/**
 * Custom type for Customer
 */
public class Customer {
    private final String id;
    private final String username;
    private final String firstname;
    private final String lastname;
    private final String email;
    private final String phoneNum;
    private final String birthDate;
    private final boolean admin;

    public Customer(String id, String username, String firstName, String lastName, String email, String phoneNum, String birthDate, boolean admin) {
        this.id = id;
        this.username = username;
        this.firstname = firstName;
        this.lastname = lastName;
        this.email = email;
        this.phoneNum = phoneNum;
        this.birthDate = birthDate;
        this.admin = admin;
    }

    public String getId()
    {
        return id;
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

    public String getEmail() {
        return email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public boolean isAdmin() {
        return admin;
    }

    @Override
    public String toString() {
        return "Customer{" +
                ", username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", admin=" + admin +
                '}';
    }
}
