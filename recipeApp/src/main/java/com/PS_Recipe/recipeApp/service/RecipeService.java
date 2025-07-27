package com.PS_Recipe.recipeApp.service;

import com.PS_Recipe.recipeApp.dto.RecipeDTO;
import com.PS_Recipe.recipeApp.dto.RecipeResponseDTO;
import com.PS_Recipe.recipeApp.entity.Recipe;
import com.PS_Recipe.recipeApp.repository.RecipeRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RestTemplate restTemplate;

    @Value("${spring.app.baseURL}")
    private String baseURL;

    @Autowired
    private EntityManager entityManager;

    @Retry(name = "dummyApi", fallbackMethod = "fallbackRecipes")
    @CircuitBreaker(name = "dummyApi", fallbackMethod = "fallbackRecipes")
    public String fetchAndSaveRecipes(){
        log.info("fetchAndSaveRecipes service function is called");


        String urlWithParams = UriComponentsBuilder.fromUriString(baseURL)
                .queryParam("limit", 50)
                .toUriString();

        RecipeResponseDTO response = restTemplate.getForObject(urlWithParams,RecipeResponseDTO.class);

        if (response==null || response.getRecipes()==null){
            return "Failed to fetch data";
        }

        List<Recipe> recipes=response.getRecipes().stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());

        recipeRepository.saveAll(recipes);
        return "Saved " + recipes.size() + " recipes.";
    }

    private Recipe convertToEntity(RecipeDTO dto){
        log.info("convertToEntity service function is called");
        return new Recipe(
                dto.getId(), dto.getName(), dto.getPrepTimeMinutes(), dto.getCookTimeMinutes(),
                dto.getServings(), dto.getDifficulty(), dto.getCuisine(), dto.getCaloriesPerServing(),
                dto.getUserId(), dto.getImage(), dto.getRating(), dto.getReviewCount(),
                dto.getIngredients(), dto.getInstructions(), dto.getTags(), dto.getMealType()
        );
    }

    @Transactional
    public void initSearchIndexing(){
        log.info("initSearchIndexing service function is called");
        Search.session((Session) entityManager)
                .massIndexer()
                .start()
                .thenRun(() -> System.out.println("Indexing Complete"));
    }


    public String fallbackRecipes(Exception e) {
        log.warn("Fallback called due to exception: {}", e.getMessage());
        return "Fallback: Could not fetch recipes";
    }
}

