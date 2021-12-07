package hu.elte.bankapp.webdomain;

import hu.elte.bankapp.configuration.PasswordMatches;
import hu.elte.bankapp.configuration.PhoneValidator;
import hu.elte.bankapp.configuration.ValidEmail;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

@PasswordMatches.List({
        @PasswordMatches(
                field = "password",
                fieldMatch = "rePassword",
                message = "Passwords do not match!"
        )
})
public class RegisterView {
    @NotNull
    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 3, max = 100, message
            = "Name must be between 3 and 100 characters")
    private String name;

    @NotNull
    @NotEmpty(message = "Email cannot be empty")
    @ValidEmail
    private String email;

    @NotNull
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 3, max = 100, message
            = "Password must be between 3 and 100 characters")
    private String password;

    @NotNull
    @NotEmpty(message = "Repeated password cannot be empty")
    private String rePassword;

    @NotNull
    @NotEmpty(message = "Phone Number cannot be empty")
    @PhoneValidator
    private  String phoneNum;

    @NotNull
    @NotEmpty(message = "Address cannot be empty")
    private  String address;

    @Past(message = "Date of birth must be in the past")
    @NotNull(message = "Birthday cannot be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    public RegisterView() {}

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
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

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "RegisterView{" +
                "Name='" + name + '\'' +
                ", emailAddress='" + email + '\'' +
                ", password='" + password + '\'' +
                ", rePassword='" + rePassword + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", address='" + address + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
