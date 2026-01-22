package org.example.coffeeshop.service;

import org.example.coffeeshop.dto.CategoryForm;
import org.example.coffeeshop.dto.ItemForm;
import org.example.coffeeshop.exception.ConflictException;
import org.example.coffeeshop.exception.ResourceNotFoundException;
import org.example.coffeeshop.model.Category;
import org.example.coffeeshop.model.MenuItem;
import org.example.coffeeshop.repository.CategoryRepository;
import org.example.coffeeshop.repository.MenuItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    private static final Logger log = LoggerFactory.getLogger(MenuService.class);

    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final FileStorageService fileStorageService;

    public MenuService(
            CategoryRepository categoryRepository,
            MenuItemRepository menuItemRepository,
            FileStorageService fileStorageService) {
        this.categoryRepository = categoryRepository;
        this.menuItemRepository = menuItemRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional(readOnly = true)
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Category> getCategoriesByLanguage(String language) {
        return categoryRepository.findByLanguageOrderByNameAsc(normalizeLanguage(language));
    }

    @Transactional(readOnly = true)
    public List<MenuItem> getItems() {
        return menuItemRepository.findAllWithCategory();
    }

    @Transactional(readOnly = true)
    public List<MenuItem> getItemsByCategory(Long categoryId) {
        return menuItemRepository.findByCategoryIdOrderByNameAsc(categoryId);
    }

    @Transactional(readOnly = true)
    public Optional<Category> getCategory(Long id) {
        return categoryRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<MenuItem> getMenuItem(Long id) {
        return menuItemRepository.findById(id);
    }

    @Transactional
    public Category createCategory(CategoryForm form) {
        Category category = new Category();
        category.setName(form.getName().trim());
        category.setLanguage(normalizeLanguage(form.getLanguage()));
        String filename = fileStorageService.storeFile(form.getImage());
        category.setImageFilename(filename);
        categoryRepository.save(category);
        log.info("event=category_create id={} hasImage={}",
                category.getId(), category.getImageFilename() != null);
        return category;
    }

    @Transactional
    public MenuItem createItem(ItemForm form) {
        Category category = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", form.getCategoryId()));

        MenuItem item = new MenuItem();
        item.setCategory(category);
        item.setName(form.getName().trim());
        item.setDescription(trimToNull(form.getDescription()));
        item.setPrice(form.getPrice());
        String filename = fileStorageService.storeFile(form.getImage());
        item.setImageFilename(filename);

        menuItemRepository.save(item);
        log.info("event=menu_item_create id={} categoryId={} hasImage={}",
                item.getId(), category.getId(), item.getImageFilename() != null);
        return item;
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
        if (menuItemRepository.existsByCategoryId(id)) {
            throw new ConflictException("Category has menu items. Delete items first.");
        }
        if (category.getImageFilename() != null) {
            fileStorageService.deleteFile(category.getImageFilename());
        }
        categoryRepository.delete(category);
        log.info("event=category_delete id={}", id);
    }

    @Transactional
    public void deleteItem(Long id) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", id));
        if (item.getImageFilename() != null) {
            fileStorageService.deleteFile(item.getImageFilename());
        }
        menuItemRepository.delete(item);
        log.info("event=menu_item_delete id={}", id);
    }

    private String normalizeLanguage(String lang) {
        if (lang == null) {
            return "ru";
        }
        String normalized = lang.trim().toLowerCase();
        return ("en".equals(normalized) ? "en" : "ru");
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
