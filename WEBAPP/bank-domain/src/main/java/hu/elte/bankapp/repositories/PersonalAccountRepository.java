package hu.elte.bankapp.repositories;

import hu.elte.bankapp.entities.PersonalAccount;
import org.springframework.data.repository.CrudRepository;

public interface PersonalAccountRepository extends CrudRepository<PersonalAccount, Integer> {
    PersonalAccount findByAccountNumber(String AccountNumber);
    Iterable<PersonalAccount> findByUserEmail(String email);
}
