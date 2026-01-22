package org.example.coffeeshop.controller.api;

import jakarta.validation.Valid;
import org.example.coffeeshop.dto.CategoryForm;
import org.example.coffeeshop.dto.ItemForm;
import org.example.coffeeshop.model.Category;
import org.example.coffeeshop.model.MenuItem;
import org.example.coffeeshop.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST API controller for admin operations.
 * Handles CRUD operations for categories and menu items.
 */
@RestController
@RequestMapping("/api/admin")
public class ApiController {

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);

    private final MenuService menuService;

    public ApiController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * Create a new category.
     *
     * @param form category data with optional image
     * @return created category ID
     */
    @PostMapping(path = "/category", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createCategory(@Valid @ModelAttribute CategoryForm form) {
        log.info("event=category_create_request hasImage={}", form.getImage() != null && !form.getImage().isEmpty());
        Category category = menuService.createCategory(form);
        return ResponseEntity.ok(Map.of("id", category.getId()));
    }

    /**
     * Create a new menu item.
     *
     * @param form item data with optional image
     * @return created item ID
     */
    @PostMapping(path = "/item", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createItem(@Valid @ModelAttribute ItemForm form) {
        log.info("event=menu_item_create_request categoryId={} hasImage={}",
                form.getCategoryId(), form.getImage() != null && !form.getImage().isEmpty());
        MenuItem item = menuService.createItem(form);
        return ResponseEntity.ok(Map.of("id", item.getId()));
    }

    /**
     * Delete a category by ID.
     *
     * @param id category ID
     * @return empty response on success
     */
    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        menuService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a menu item by ID.
     *
     * @param id menu item ID
     * @return empty response on success
     */
    @DeleteMapping("/item/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        menuService.deleteItem(id);
        return ResponseEntity.ok().build();
    }
}
