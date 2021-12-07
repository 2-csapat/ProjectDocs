package hu.elte.bankapp.webdomain;

import hu.elte.bankapp.configuration.PasswordMatches;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@PasswordMatches.List({
        @PasswordMatches(
                field = "password",
                fieldMatch = "rePassword",
                message = "Passwords do not match!"
        )
})
public class ForgotView {

    @NotNull
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 3, max = 100, message
            = "Password must be between 3 and 100 characters")
    private String password;

    @NotNull
    @NotEmpty(message = "Repeated password cannot be empty")
    private String rePassword;

    public ForgotView() { }

    public String getPassword() {
        return password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    @Override
    public String toString() {
        return "ForgotView{" +
                ", password='" + password + '\'' +
                ", rePassword='" + rePassword + '\'' +
                '}';
    }
}

