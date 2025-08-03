package com.PS_Recipe.recipeApp.dto;

import com.PS_Recipe.recipeApp.entity.Favourite;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String username;
    private List<Favourite> favourites;
}
