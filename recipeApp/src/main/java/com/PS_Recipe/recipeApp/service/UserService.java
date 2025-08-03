package com.PS_Recipe.recipeApp.service;

import com.PS_Recipe.recipeApp.entity.Favourite;
import com.PS_Recipe.recipeApp.entity.User;
import com.PS_Recipe.recipeApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public void saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void addFavouriteToUser(ObjectId userId, Favourite favourite) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getFavourites() == null) {
                user.setFavourites(new ArrayList<>());
            }
            user.getFavourites().add(favourite);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }

    public void removeFavouriteFromUser(String userId, String favouriteName) {
        User user = userRepository.findById(new ObjectId(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getFavourites() != null) {
            user.getFavourites().removeIf(fav -> fav.getName().equalsIgnoreCase(favouriteName));
            userRepository.save(user);
        }
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

}
