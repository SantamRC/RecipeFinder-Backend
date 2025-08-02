package com.PS_Recipe.recipeApp.controller;

import com.PS_Recipe.recipeApp.entity.User;
import com.PS_Recipe.recipeApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAll();
    }

    @PostMapping
    public String createUser(@RequestBody User user){
        userService.saveUser(user);
        return "Done";
    }

}
