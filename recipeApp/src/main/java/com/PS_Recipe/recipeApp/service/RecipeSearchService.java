package com.PS_Recipe.recipeApp.service;

import com.PS_Recipe.recipeApp.entity.Recipe;
import com.PS_Recipe.recipeApp.repository.RecipeRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeSearchService {

    private final EntityManager entityManager;
    private final RecipeRepository recipeRepository;


    @Transactional
    public List<Recipe> searchByNameOrCuisine(String query) {
        log.info("searchByNameOrCuisine service function is called");
        var session = Search.session(entityManager.unwrap(Session.class));

        if (query == null || query.trim().isEmpty()) {
            return session.search(Recipe.class)
                    .where(f -> f.matchAll())
                    .fetchAllHits();
        }

        return session.search(Recipe.class)
                .where(f -> f.match()
                        .fields("name", "cuisine")
                        .matching(query)
                        .fuzzy(1))
                .fetchHits(20);
    }

    public ResponseEntity<Recipe> searchById(String id){
        log.info("searchById service function is called");
        return recipeRepository.findById(Long.valueOf(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
