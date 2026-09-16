package com.example.userauthenticationsevice.controller;

import com.example.userauthenticationsevice.dtos.LogOutRequestDto;
import com.example.userauthenticationsevice.dtos.LoginRequestDto;
import com.example.userauthenticationsevice.dtos.SignUpRequestDto;
import com.example.userauthenticationsevice.dtos.UserDto;
import com.example.userauthenticationsevice.exception.UserAlreadyExistsException;
import com.example.userauthenticationsevice.models.User;
import com.example.userauthenticationsevice.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        if(signUpRequestDto.getEmail() == null || "".equals(signUpRequestDto.getEmail()) || signUpRequestDto.getPassword() == null || "".equals(signUpRequestDto.getPassword())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = authService.signUp(signUpRequestDto.getEmail(),  signUpRequestDto.getPassword());
        return new ResponseEntity<>(from(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        User user =  authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        return new ResponseEntity<>(from(user), HttpStatus.OK);
    }

    @PostMapping("logout")
    public ResponseEntity<UserDto> logout(@RequestBody LogOutRequestDto  logOutRequestDto) {
        User user =  authService.logout(logOutRequestDto.getEmail());
        return new ResponseEntity<>(from(user), HttpStatus.OK);
    }

    private UserDto from(User user){
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRole());
        return userDto;
    }


}
