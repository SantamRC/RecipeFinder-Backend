package com.PS_Recipe.recipeApp.service;

import com.PS_Recipe.recipeApp.dto.RecipeDTO;
import com.PS_Recipe.recipeApp.dto.RecipeResponseDTO;
import com.PS_Recipe.recipeApp.entity.Recipe;
import com.PS_Recipe.recipeApp.repository.RecipeRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeServiceTest {

    @InjectMocks
    private RecipeService recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(recipeService, "baseURL", "https://dummyjson.com/recipes");
    }

    @Test
    void fetchAndSaveRecipes_shouldFetchAndSaveRecipes() {
        RecipeDTO dto = new RecipeDTO();
        dto.setId(1L);
        dto.setName("Test Pizza");

        RecipeResponseDTO responseDTO = new RecipeResponseDTO();
        responseDTO.setRecipes(List.of(dto));

        when(restTemplate.getForObject(anyString(), eq(RecipeResponseDTO.class))).thenReturn(responseDTO);

        String result = recipeService.fetchAndSaveRecipes();

        assertEquals("Saved 1 recipes.", result);
        verify(recipeRepository, times(1)).saveAll(anyList());
    }

    @Test
    void fetchAndSaveRecipes_shouldHandleNullResponse() {
        when(restTemplate.getForObject(anyString(), eq(RecipeResponseDTO.class))).thenReturn(null);

        String result = recipeService.fetchAndSaveRecipes();

        assertEquals("Failed to fetch data", result);
        verify(recipeRepository, never()).saveAll(any());
    }

    @Test
    void fallbackRecipes_shouldReturnFallbackMessage() {
        String fallback = recipeService.fallbackRecipes(new RuntimeException("API failure"));
        assertEquals("Fallback: Could not fetch recipes", fallback);
    }

    @Test
    void initSearchIndexing_shouldTriggerIndexing() throws Exception {
        SearchSession mockSearchSession = mock(SearchSession.class);
        MassIndexer mockIndexer = mock(MassIndexer.class);

        // Use Mockito to mock static method: Search.session()
        try (MockedStatic<Search> searchMockedStatic = mockStatic(Search.class)) {
            searchMockedStatic.when(() -> Search.session(entityManager)).thenReturn(mockSearchSession);

            when(mockSearchSession.massIndexer()).thenReturn(mockIndexer);
            when(mockIndexer.start()).thenReturn(CompletableFuture.completedFuture(null));

            recipeService.initSearchIndexing();

            verify(mockIndexer, times(1)).start();
        }
    }
}
