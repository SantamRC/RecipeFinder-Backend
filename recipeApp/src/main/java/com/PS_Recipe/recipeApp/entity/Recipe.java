package com.PS_Recipe.recipeApp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

import java.util.List;

@Entity
@Indexed
@Access(AccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @Id
    private Long id;

    @Size(min=1, message = "Name cannot be blank")
    @FullTextField
    private String name;

    @NotNull
    private Integer prepTimeMinutes;
    @NotNull
    private Integer cookTimeMinutes;
    @NotNull
    private Integer servings;
    @NotNull
    private String difficulty;

    @Size(min = 1, message = "Cuisine cannot be blank")
    @FullTextField
    private String cuisine;

    private Integer caloriesPerServing;
    private Integer userId;
    private String image;
    @NotNull
    private Double rating;
    private Integer reviewCount;

    @Size(min = 1, message = "Ingredients cannot be blank")
    @ElementCollection
    private List<String> ingredients;

    @ElementCollection
    private List<String> instructions;

    @ElementCollection
    private List<String> tags;

    @ElementCollection
    private List<String> mealType;
}

