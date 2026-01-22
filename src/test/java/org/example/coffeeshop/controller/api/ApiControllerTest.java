package org.example.coffeeshop.controller.api;

import org.example.coffeeshop.exception.ConflictException;
import org.example.coffeeshop.exception.GlobalExceptionHandler;
import org.example.coffeeshop.exception.InvalidFileException;
import org.example.coffeeshop.exception.ResourceNotFoundException;
import org.example.coffeeshop.model.Category;
import org.example.coffeeshop.model.MenuItem;
import org.example.coffeeshop.service.MenuService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Slice tests for ApiController.
 * Tests REST API endpoints in isolation.
 */
@WebMvcTest(ApiController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    @Test
    @DisplayName("POST /api/admin/category - should create category with valid data")
    void createCategory_withValidData_shouldCreateCategory() throws Exception {
        when(menuService.createCategory(any())).thenAnswer(invocation -> {
            Category c = new Category();
            c.setId(1L);
            return c;
        });

        MockMultipartFile image = new MockMultipartFile(
                "image", "coffee.jpg", "image/jpeg", "fake-image-data".getBytes());

        mockMvc.perform(multipart("/api/admin/category")
                        .file(image)
                        .param("name", "Coffee")
                        .param("language", "ru"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("POST /api/admin/category - should reject invalid file type")
    void createCategory_withInvalidFileType_shouldReject() throws Exception {
        when(menuService.createCategory(any()))
                .thenThrow(new InvalidFileException("Invalid file type. Allowed types: JPEG, PNG, GIF, WebP"));

        MockMultipartFile invalidFile = new MockMultipartFile(
                "image", "script.exe", "application/x-msdownload", "malicious".getBytes());

        mockMvc.perform(multipart("/api/admin/category")
                        .file(invalidFile)
                        .param("name", "Coffee")
                        .param("language", "ru"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("DELETE /api/admin/category/{id} - should delete existing category")
    void deleteCategory_existingCategory_shouldDelete() throws Exception {
        doNothing().when(menuService).deleteCategory(1L);

        mockMvc.perform(delete("/api/admin/category/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/admin/category/{id} - should return 404 for non-existing category")
    void deleteCategory_nonExistingCategory_shouldReturn404() throws Exception {
        doThrow(new ResourceNotFoundException("Category", 999L))
                .when(menuService).deleteCategory(999L);

        mockMvc.perform(delete("/api/admin/category/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/admin/category/{id} - should return 409 when category has items")
    void deleteCategory_withItems_shouldReturn409() throws Exception {
        doThrow(new ConflictException("Category has menu items. Delete items first."))
                .when(menuService).deleteCategory(1L);

        mockMvc.perform(delete("/api/admin/category/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /api/admin/item - should create item with valid data")
    void createItem_withValidData_shouldCreateItem() throws Exception {
        when(menuService.createItem(any())).thenAnswer(invocation -> {
            MenuItem item = new MenuItem();
            item.setId(1L);
            return item;
        });

        mockMvc.perform(multipart("/api/admin/item")
                        .param("categoryId", "1")
                        .param("name", "Latte")
                        .param("price", "150.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("POST /api/admin/item - should return 404 when category not found")
    void createItem_withNonExistingCategory_shouldReturn404() throws Exception {
        when(menuService.createItem(any()))
                .thenThrow(new ResourceNotFoundException("Category", 999L));

        mockMvc.perform(multipart("/api/admin/item")
                        .param("categoryId", "999")
                        .param("name", "Latte")
                        .param("price", "150.00"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/admin/item/{id} - should delete existing item")
    void deleteItem_existingItem_shouldDelete() throws Exception {
        doNothing().when(menuService).deleteItem(1L);

        mockMvc.perform(delete("/api/admin/item/1"))
                .andExpect(status().isOk());
    }
}
