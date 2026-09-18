package com.example.userauthenticationsevice.services;

import com.example.userauthenticationsevice.dtos.ValidateTokenDto;
import com.example.userauthenticationsevice.exception.InvalidCredentialsException;
import com.example.userauthenticationsevice.exception.UserAlreadyExistsException;
import com.example.userauthenticationsevice.models.User;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public interface IAuthService {
    User signUp(String email, String password) throws UserAlreadyExistsException;
    Pair<User, MultiValueMap<String, String>> login(String email, String password) throws InvalidCredentialsException;
    User logout(String email);
    Boolean validateToken(ValidateTokenDto validateTokenDto);
}
