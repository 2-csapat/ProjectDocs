package hu.elte.bankapp.repositories;

import hu.elte.bankapp.entities.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Integer> {

}
