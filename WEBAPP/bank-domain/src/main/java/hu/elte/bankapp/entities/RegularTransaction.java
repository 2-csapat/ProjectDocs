package hu.elte.bankapp.entities;

import javax.persistence.Entity;

@Entity
public class RegularTransaction extends Transaction{
    @Override
    public String toString() {
        return "regularTransaction{} " + super.toString();
    }
}
