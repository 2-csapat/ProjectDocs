package hu.elte.bankapp.repositories;

import hu.elte.bankapp.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    @Query(value = "SELECT * FROM USER u WHERE u.email =?1", nativeQuery = true)
    User findByEmail(String email);
    User findByResetPasswordToken(String token);
}
