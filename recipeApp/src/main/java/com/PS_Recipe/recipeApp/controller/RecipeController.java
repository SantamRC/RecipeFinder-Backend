package com.PS_Recipe.recipeApp.controller;

import com.PS_Recipe.recipeApp.entity.Recipe;
import com.PS_Recipe.recipeApp.repository.RecipeRepository;
import com.PS_Recipe.recipeApp.service.RecipeSearchService;
import com.PS_Recipe.recipeApp.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;
    private final RecipeSearchService searchService;
    private final RecipeRepository recipeRepository;


    @PostMapping("/fetch-and-save")
    public String fetchAndSaveRecipes(){
        log.info("Called the /fetch-and-save endpoint");
        return recipeService.fetchAndSaveRecipes();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipes(@Valid @RequestParam(required = false) String query){
        log.info("Called the /search endpoint");
        return ResponseEntity.ok(searchService.searchByNameOrCuisine(query));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Integer id){
        log.info("Called the /{id} endpoint");
        return searchService.searchById(String.valueOf(id));
    }
}
