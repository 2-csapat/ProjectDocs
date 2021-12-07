package hu.elte.bankapp.entities;

import javax.persistence.Entity;

@Entity
public class SavingAccount extends Account{
    @Override
    public String toString() {
        return "SavingAccount{} " + super.toString();
    }
}
