package com.PS_Recipe.recipeApp.repository;

import com.PS_Recipe.recipeApp.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe,Long> {
}
