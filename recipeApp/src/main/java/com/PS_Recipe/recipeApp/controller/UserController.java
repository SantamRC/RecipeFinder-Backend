package com.PS_Recipe.recipeApp.controller;

import com.PS_Recipe.recipeApp.dto.LoginRequestDTO;
import com.PS_Recipe.recipeApp.dto.LoginResponseDTO;
import com.PS_Recipe.recipeApp.entity.Favourite;
import com.PS_Recipe.recipeApp.entity.User;
import com.PS_Recipe.recipeApp.service.UserService;
import com.PS_Recipe.recipeApp.utility.JwtUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

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


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        User user = userService.getUserByUsername(request.getUsername());

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(new LoginResponseDTO(token,user.getUsername(),user.getFavourites() != null ? user.getFavourites() : List.of()));
    }

}
