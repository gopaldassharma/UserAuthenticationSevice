package com.example.userauthenticationsevice.controller;

import com.example.userauthenticationsevice.dtos.*;
import com.example.userauthenticationsevice.exception.InvalidCredentialsException;
import com.example.userauthenticationsevice.exception.UserAlreadyExistsException;
import com.example.userauthenticationsevice.models.User;
import com.example.userauthenticationsevice.services.IAuthService;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) throws UserAlreadyExistsException {
        if (signUpRequestDto.getEmail() == null || "".equals(signUpRequestDto.getEmail()) || signUpRequestDto.getPassword() == null || "".equals(signUpRequestDto.getPassword())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = authService.signUp(signUpRequestDto.getEmail(), signUpRequestDto.getPassword());
        return new ResponseEntity<>(from(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto) throws InvalidCredentialsException {
        Pair<User, MultiValueMap<String, String>> p = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        if (p.a == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(from(p.a), p.b, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<UserDto> logout(@RequestBody LogOutRequestDto logOutRequestDto) {
        User user = authService.logout(logOutRequestDto.getEmail());
        return new ResponseEntity<>(from(user), HttpStatus.OK);
    }

    @PostMapping("/validateToken")
    public Boolean validate(@RequestBody ValidateTokenDto validateTokenDto) throws InvalidCredentialsException {
        Boolean result = authService.validateToken(validateTokenDto);
        if (!result) {
            throw new InvalidCredentialsException("Token is expired");
        }
        return true;
    }

    private UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRole());
        return userDto;
    }
}

