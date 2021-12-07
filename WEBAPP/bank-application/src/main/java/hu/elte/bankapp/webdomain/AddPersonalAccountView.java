package hu.elte.bankapp.webdomain;

public class AddPersonalAccountView {
    String name;
    int dailyTransferLimit;

    public AddPersonalAccountView() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDailyTransferLimit() {
        return dailyTransferLimit;
    }

    public void setDailyTransferLimit(int dailyTransferLimit) {
        this.dailyTransferLimit = dailyTransferLimit;
    }
}
