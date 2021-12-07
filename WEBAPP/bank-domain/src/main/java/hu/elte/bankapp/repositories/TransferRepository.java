package hu.elte.bankapp.repositories;

import hu.elte.bankapp.entities.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransferRepository extends CrudRepository<Transaction, Integer> {

}
