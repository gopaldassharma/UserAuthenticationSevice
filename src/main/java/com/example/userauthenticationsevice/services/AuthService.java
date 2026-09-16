package com.example.userauthenticationsevice.services;

import com.example.userauthenticationsevice.exception.UserAlreadyExistsException;
import com.example.userauthenticationsevice.models.User;
import com.example.userauthenticationsevice.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements IAuthService{

    @Autowired
    UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User signUp(String email, String password) throws UserAlreadyExistsException {
        Optional<User> userOptional = userRepo.findUserByEmail(email);
        if(userOptional.isPresent()){
            throw new UserAlreadyExistsException("Email Aleady registered");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepo.save(user);
        return user;
    }

    @Override
    public User login(String email, String password) {
        return null;
    }

    @Override
    public User logout(String email) {
        return null;
    }
}
