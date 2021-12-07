package hu.elte.bankapp.repositories;

import hu.elte.bankapp.entities.PersonalAccount;
import hu.elte.bankapp.entities.SavingAccount;
import org.springframework.data.repository.CrudRepository;

public interface SavingAccountRepository extends CrudRepository<SavingAccount, Integer> {
    SavingAccount findByAccountNumber(String AccountNumber);
    Iterable<SavingAccount> findByUserEmail(String email);
}
