package hu.elte.bankapp.repositories;

import hu.elte.bankapp.entities.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    Customer findByEmail(String Email);
}
