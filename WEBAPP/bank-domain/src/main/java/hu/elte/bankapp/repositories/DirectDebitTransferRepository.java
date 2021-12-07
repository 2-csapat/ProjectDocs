package hu.elte.bankapp.repositories;

import hu.elte.bankapp.entities.DirectDebitTransaction;
import org.springframework.data.repository.CrudRepository;

public interface DirectDebitTransferRepository extends CrudRepository<DirectDebitTransaction, Integer> {
}
