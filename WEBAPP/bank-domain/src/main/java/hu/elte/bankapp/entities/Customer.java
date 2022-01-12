package hu.elte.bankapp.entities;

import javax.persistence.Entity;
import java.time.LocalDate;


/**
 * .
 */
@Entity
public class Customer extends User {
    private String phoneNum;
    private String address;
    private LocalDate dateOfBirth;


    //Geters
    public String getPhoneNum() {
        return phoneNum;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getDateOfBirth(LocalDate dateOfBirth) {
        return this.dateOfBirth;
    }

    //Setters
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * 
     */
    //private List<Account> accounts;

    @Override
    public String toString() {
        return super.toString()  + phoneNum + '\'' +
                ", address='" + address + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}