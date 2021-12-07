package hu.elte.bankapp.entities;

import javax.persistence.Entity;

@Entity
public class SavedTransaction extends Transaction{
    @Override
    public String toString() {
        return "SavedTransaction{} " + super.toString();
    }
}
