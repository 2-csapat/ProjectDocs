package hu.elte.bankapp.entities;

import javax.persistence.Entity;

@Entity
public class PersonalAccount extends Account {
    private int dailyTransferLimit;

    public int getDailyTransferLimit() {
        return dailyTransferLimit;
    }

    public void setDailyTransferLimit(int dailyTransferLimit) {
        this.dailyTransferLimit = dailyTransferLimit;
    }

    @Override
    public String toString() {
        return super.toString() +
                "dailyTransferLimit=" + dailyTransferLimit +
                '}';
    }
}
