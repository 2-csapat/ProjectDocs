package hu.elte.bankapp.repositories;

import hu.elte.bankapp.entities.SavedTransaction;
import org.springframework.data.repository.CrudRepository;

public interface SavedTransferRepository extends CrudRepository<SavedTransaction, Integer> {
    SavedTransaction findById(int Id);
    SavedTransaction findByComment(String comment);
}
