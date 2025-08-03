package com.PS_Recipe.recipeApp.controller;

import com.PS_Recipe.recipeApp.entity.Favourite;
import com.PS_Recipe.recipeApp.entity.User;
import com.PS_Recipe.recipeApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/{userId}/favourites")
    public String addFavourite(@PathVariable String userId, @RequestBody Favourite favourite) {
        userService.addFavouriteToUser(new ObjectId(userId), favourite);
        return "Favourite added";
    }

    @DeleteMapping("/{id}/favourites")
    public ResponseEntity<String> deleteFavourite(
            @PathVariable String id,
            @RequestParam String name) {
        userService.removeFavouriteFromUser(id, name);
        return ResponseEntity.ok("Favourite removed!");
    }

}
