package hu.elte.bankapp.service;

import hu.elte.bankapp.entities.User;
import hu.elte.bankapp.exception.UserNotFoundException;
import hu.elte.bankapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //public User getUserByEmail(String email){
       // return userRepository.findByEmail(email);
    //}

    public  void updateResetPasswordToken(String token, String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);

        if(user != null){
            user.setResetPasswordToken(token);
            userRepository.save(user);
        } else {
            throw new UserNotFoundException("Could not find any user with email: " + email);
        }
    }

    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }
    public void updatePassword(User user, String newPassword){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);

        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);

        userRepository.save(user);
    }
}
