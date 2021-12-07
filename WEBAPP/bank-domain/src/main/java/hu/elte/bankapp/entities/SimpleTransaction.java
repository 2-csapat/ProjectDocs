package hu.elte.bankapp.entities;

import javax.persistence.Entity;

@Entity
public class SimpleTransaction extends Transaction{
    @Override
    public String toString() {
        return "SimpleTransaction{} " + super.toString();
    }
}
