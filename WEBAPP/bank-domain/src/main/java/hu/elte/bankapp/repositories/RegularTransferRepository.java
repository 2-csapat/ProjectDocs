package hu.elte.bankapp.repositories;

import hu.elte.bankapp.entities.RegularTransaction;
import hu.elte.bankapp.entities.SavedTransaction;
import org.springframework.data.repository.CrudRepository;

public interface RegularTransferRepository extends CrudRepository<RegularTransaction, Integer> {
    RegularTransaction findById(int id);
}
