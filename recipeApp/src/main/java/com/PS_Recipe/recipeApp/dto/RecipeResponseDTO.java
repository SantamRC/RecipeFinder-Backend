package com.PS_Recipe.recipeApp.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecipeResponseDTO {
    private List<RecipeDTO> recipes;
    private int total;
    private int skip;
    private int limit;
}
