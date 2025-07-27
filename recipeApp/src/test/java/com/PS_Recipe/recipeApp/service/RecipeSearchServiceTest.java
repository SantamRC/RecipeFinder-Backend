package com.PS_Recipe.recipeApp.service;

import com.PS_Recipe.recipeApp.entity.Recipe;
import com.PS_Recipe.recipeApp.repository.RecipeRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.search.engine.search.predicate.SearchPredicate;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.engine.search.query.dsl.SearchQueryOptionsStep;
import org.hibernate.search.engine.search.query.dsl.SearchQuerySelectStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeSearchServiceTest {

    @InjectMocks
    private RecipeSearchService recipeSearchService;

    @Mock
    private EntityManager entityManager;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private Session session;

    @Mock
    private SearchSession searchSession;

    @Mock
    private SearchQuerySelectStep<?, Recipe, ?, ?, ?, ?> selectStep;

    @Mock
    private SearchQueryOptionsStep<?, Recipe, ?,?,?> optionsStep;

    @Mock
    private SearchResult<Recipe> searchResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchById_found() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        ResponseEntity<Recipe> response = recipeSearchService.searchById("1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(recipe, response.getBody());
    }

    @Test
    void testSearchById_notFound() {
        when(recipeRepository.findById(2L)).thenReturn(Optional.empty());

        ResponseEntity<Recipe> response = recipeSearchService.searchById("2");

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testSearchByNameOrCuisine_emptyQuery() {
        Recipe recipe = new Recipe();
        when(entityManager.unwrap(Session.class)).thenReturn(session);
        when(Search.session(session)).thenReturn(searchSession);
//        when(searchSession.search(Recipe.class)).thenReturn(selectStep);
//        when(selectStep.where((SearchPredicate) any())).thenReturn(optionsStep);
        when(optionsStep.fetchAllHits()).thenReturn(List.of(recipe));

        List<Recipe> result = recipeSearchService.searchByNameOrCuisine("");

        assertEquals(1, result.size());
        assertEquals(recipe, result.get(0));
    }
}
