package org.example.coffeeshop.controller;
import org.example.coffeeshop.model.Category;
import org.example.coffeeshop.model.MenuItem;
import org.example.coffeeshop.repository.CategoryRepository;
import org.example.coffeeshop.repository.MenuItemRepository;
import org.example.coffeeshop.service.FileStorageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final FileStorageService fileStorageService;
    public AdminController(CategoryRepository categoryRepository, MenuItemRepository menuItemRepository, FileStorageService fileStorageService) {
        this.categoryRepository = categoryRepository;
        this.menuItemRepository = menuItemRepository;
        this.fileStorageService = fileStorageService;
    }
    @GetMapping("/categories")
    public String listCategories(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "admin/categories";
    }
    @PostMapping("/categories/add")
    public String addCategory(@RequestParam String name, @RequestParam String language, @RequestParam(required = false) MultipartFile image) throws IOException {
        Category c = new Category();
        c.setName(name);
        c.setLanguage(language);
        if (image != null && !image.isEmpty()) {
            String filename = fileStorageService.storeFile(image);
            c.setImageFilename(filename);
        }
        categoryRepository.save(c);
        return "redirect:/admin/categories";
    }
    @PostMapping("/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        categoryRepository.findById(id).ifPresent(c -> {
            if (c.getImageFilename() != null) {
                fileStorageService.deleteFile(c.getImageFilename());
            }
            categoryRepository.delete(c);
        });
        return "redirect:/admin/categories";
    }
    @GetMapping("/items")
    public String listItems(Model model) {
        List<MenuItem> items = menuItemRepository.findAll();
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("items", items);
        model.addAttribute("categories", categories);
        return "admin/items";
    }
    @PostMapping("/items/add")
    public String addItem(@RequestParam String name, @RequestParam String description, @RequestParam String price, @RequestParam Long categoryId, @RequestParam(required = false) MultipartFile image) throws IOException {
        MenuItem it = new MenuItem();
        it.setName(name);
        it.setDescription(description);
        it.setPrice(new BigDecimal(price));
        if (image != null && !image.isEmpty()) {
            String filename = fileStorageService.storeFile(image);
            it.setImageFilename(filename);
        }
        Category c = categoryRepository.findById(categoryId).orElse(null);
        it.setCategory(c);
        menuItemRepository.save(it);
        return "redirect:/admin/items";
    }
    @PostMapping("/items/{id}/delete")
    public String deleteItem(@PathVariable Long id) {
        menuItemRepository.findById(id).ifPresent(item -> {
            if (item.getImageFilename() != null) {
                fileStorageService.deleteFile(item.getImageFilename());
            }
            menuItemRepository.delete(item);
        });
        return "redirect:/admin/items";
    }
}
