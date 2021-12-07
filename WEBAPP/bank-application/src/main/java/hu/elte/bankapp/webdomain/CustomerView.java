package hu.elte.bankapp.webdomain;

import java.util.Date;

public class CustomerView {
    private  String email;
    private  String name;
    private  String phoneNum;
    private  String address;
    private  Date dateOfBirth;

    public CustomerView(String email, String name, String phoneNum, String address, Date dateOfBirth) {
        this.email = email;
        this.name = name;
        this.phoneNum = phoneNum;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    public CustomerView() {
    }


    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getAddress() {
        return address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }
}
