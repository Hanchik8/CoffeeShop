package org.example.coffeeshop.service;

import org.example.coffeeshop.dto.CategoryForm;
import org.example.coffeeshop.dto.ItemForm;
import org.example.coffeeshop.exception.ConflictException;
import org.example.coffeeshop.exception.ResourceNotFoundException;
import org.example.coffeeshop.model.Category;
import org.example.coffeeshop.model.MenuItem;
import org.example.coffeeshop.repository.CategoryRepository;
import org.example.coffeeshop.repository.MenuItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MenuServiceTest {

    @Test
    @DisplayName("createCategory should trim names and normalize language")
    void createCategory_shouldTrimNameAndNormalizeLanguage() {
        CategoryRepository categoryRepository = mock(CategoryRepository.class);
        MenuItemRepository menuItemRepository = mock(MenuItemRepository.class);
        FileStorageService fileStorageService = mock(FileStorageService.class);
        MenuService menuService = new MenuService(categoryRepository, menuItemRepository, fileStorageService);

        CategoryForm form = new CategoryForm();
        form.setName(" Coffee ");
        form.setLanguage("EN");
        form.setImage(null);

        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category category = invocation.getArgument(0);
            category.setId(10L);
            return category;
        });

        Category created = menuService.createCategory(form);

        assertThat(created.getName()).isEqualTo("Coffee");
        assertThat(created.getLanguage()).isEqualTo("en");
        verify(fileStorageService).storeFile(isNull());
    }

    @Test
    @DisplayName("createItem should throw when category is missing")
    void createItem_shouldThrowWhenCategoryMissing() {
        CategoryRepository categoryRepository = mock(CategoryRepository.class);
        MenuItemRepository menuItemRepository = mock(MenuItemRepository.class);
        FileStorageService fileStorageService = mock(FileStorageService.class);
        MenuService menuService = new MenuService(categoryRepository, menuItemRepository, fileStorageService);

        ItemForm form = new ItemForm();
        form.setCategoryId(99L);
        form.setName("Latte");
        form.setPrice(new BigDecimal("150.00"));

        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuService.createItem(form))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("createItem should store item with resolved category")
    void createItem_shouldStoreItem() {
        CategoryRepository categoryRepository = mock(CategoryRepository.class);
        MenuItemRepository menuItemRepository = mock(MenuItemRepository.class);
        FileStorageService fileStorageService = mock(FileStorageService.class);
        MenuService menuService = new MenuService(categoryRepository, menuItemRepository, fileStorageService);

        Category category = new Category();
        category.setId(3L);

        ItemForm form = new ItemForm();
        form.setCategoryId(3L);
        form.setName(" Mocha ");
        form.setDescription("  rich  ");
        form.setPrice(new BigDecimal("190.00"));

        when(categoryRepository.findById(3L)).thenReturn(Optional.of(category));
        when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MenuItem created = menuService.createItem(form);

        assertThat(created.getCategory()).isEqualTo(category);
        assertThat(created.getName()).isEqualTo("Mocha");
        assertThat(created.getDescription()).isEqualTo("rich");
        verify(fileStorageService).storeFile(isNull());
    }

    @Test
    @DisplayName("deleteCategory should throw when items exist")
    void deleteCategory_shouldThrowWhenItemsExist() {
        CategoryRepository categoryRepository = mock(CategoryRepository.class);
        MenuItemRepository menuItemRepository = mock(MenuItemRepository.class);
        FileStorageService fileStorageService = mock(FileStorageService.class);
        MenuService menuService = new MenuService(categoryRepository, menuItemRepository, fileStorageService);

        Category category = new Category();
        category.setId(7L);

        doReturn(Optional.of(category)).when(categoryRepository).findById(7L);
        doReturn(true).when(menuItemRepository).existsByCategoryId(7L);

        assertThatThrownBy(() -> menuService.deleteCategory(7L))
                .isInstanceOf(ConflictException.class);
    }
}
