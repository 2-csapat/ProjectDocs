package hu.elte.bankapp.entities;

import javax.persistence.*;
import java.util.*;

/**
 * 
 */
@Entity
@Table(name = "User")
public abstract class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "EMAIL")
    private String email;
    @OneToMany(
            mappedBy = "user",
//            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private List<Account> accounts = new ArrayList<>();

    @Column(name = "RESET_PASSWORD_TOKEN")
    private String resetPasswordToken;


    //Getters
    public int getId() {
        return id;
    }

    public List<Account> getAccounts() { return accounts; }

    public void setAccounts(List<Account> accounts) { this.accounts = accounts; }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' ;
    }
}