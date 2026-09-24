package com.example.userauthenticationsevice.controller;

import com.example.userauthenticationsevice.dtos.UserDto;
import com.example.userauthenticationsevice.models.User;
import com.example.userauthenticationsevice.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepo userRepo;
    @GetMapping
    public UserDto getUser(@PathVariable Long userId) {
        User user = userRepo.findById(userId).get();
        return from(user);
    }
    private UserDto from(User user){
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRole());
        return userDto;
    }

}
