package org.example.coffeeshop.controller;

import org.example.coffeeshop.model.Category;
import org.example.coffeeshop.service.MenuService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Slice tests for PublicController.
 */
@WebMvcTest(PublicController.class)
@AutoConfigureMockMvc(addFilters = false)
class PublicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    @Test
    @DisplayName("GET / should return index page")
    void indexPage_shouldReturnIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @DisplayName("GET /ru/categories should return categories page")
    void categoriesPage_shouldReturnCategories() throws Exception {
        when(menuService.getCategoriesByLanguage("ru")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ru/categories"))
                .andExpect(status().isOk())
                .andExpect(view().name("categories"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attribute("lang", "ru"));
    }

    @Test
    @DisplayName("GET /en/categories should return categories page")
    void categoriesPage_shouldReturnEnglishCategories() throws Exception {
        when(menuService.getCategoriesByLanguage("en")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/en/categories"))
                .andExpect(status().isOk())
                .andExpect(view().name("categories"))
                .andExpect(model().attribute("lang", "en"));
    }

    @Test
    @DisplayName("GET /de/categories should redirect to /ru/categories")
    void categoriesPage_withInvalidLang_shouldRedirect() throws Exception {
        mockMvc.perform(get("/de/categories"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ru/categories"));
    }

    @Test
    @DisplayName("GET /ru/category/{id}/items should return items page for existing category")
    void itemsPage_shouldReturnItemsForExistingCategory() throws Exception {
        Category category = new Category();
        category.setId(1L);

        when(menuService.getCategory(1L)).thenReturn(Optional.of(category));
        when(menuService.getItemsByCategory(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ru/category/1/items"))
                .andExpect(status().isOk())
                .andExpect(view().name("items"))
                .andExpect(model().attribute("lang", "ru"));
    }

    @Test
    @DisplayName("GET /ru/category/{id}/items should redirect when category not found")
    void itemsPage_shouldRedirectWhenCategoryMissing() throws Exception {
        when(menuService.getCategory(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ru/category/999/items"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ru/categories"));
    }
}
