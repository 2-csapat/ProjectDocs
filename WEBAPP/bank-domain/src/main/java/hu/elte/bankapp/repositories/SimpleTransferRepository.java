package hu.elte.bankapp.repositories;

import hu.elte.bankapp.entities.SimpleTransaction;
import org.springframework.data.repository.CrudRepository;

import java.util.logging.SimpleFormatter;

public interface SimpleTransferRepository extends CrudRepository<SimpleTransaction, Integer> {
    SimpleTransaction findById(int id);
}
