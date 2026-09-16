package com.example.userauthenticationsevice.services;

import com.example.userauthenticationsevice.exception.UserAlreadyExistsException;
import com.example.userauthenticationsevice.models.User;
import org.springframework.stereotype.Service;

@Service
public interface IAuthService {
    User signUp(String email, String password) throws UserAlreadyExistsException;
    User login(String email, String password);
    User logout(String email);
}
