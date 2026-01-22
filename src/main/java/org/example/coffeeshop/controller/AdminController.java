package org.example.coffeeshop.controller;

import org.example.coffeeshop.dto.CategoryForm;
import org.example.coffeeshop.dto.ItemForm;
import org.example.coffeeshop.model.Category;
import org.example.coffeeshop.model.MenuItem;
import org.example.coffeeshop.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final MenuService menuService;

    public AdminController(MenuService menuService) {
        this.menuService = menuService;
    }
    @GetMapping("/categories")
    public String listCategories(Model model) {
        List<Category> categories = menuService.getCategories();
        model.addAttribute("categories", categories);
        return "admin/categories";
    }
    @PostMapping("/categories/add")
    public String addCategory(@RequestParam String name,
                              @RequestParam String language,
                              @RequestParam(required = false) MultipartFile image) {
        CategoryForm form = new CategoryForm();
        form.setName(name);
        form.setLanguage(language);
        form.setImage(image);
        menuService.createCategory(form);
        return "redirect:/admin/categories";
    }
    @PostMapping("/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        menuService.deleteCategory(id);
        return "redirect:/admin/categories";
    }
    @GetMapping("/items")
    public String listItems(Model model) {
        List<MenuItem> items = menuService.getItems();
        List<Category> categories = menuService.getCategories();
        model.addAttribute("items", items);
        model.addAttribute("categories", categories);
        return "admin/items";
    }
    @PostMapping("/items/add")
    public String addItem(@RequestParam String name,
                          @RequestParam(required = false) String description,
                          @RequestParam String price,
                          @RequestParam Long categoryId,
                          @RequestParam(required = false) MultipartFile image) {
        ItemForm form = new ItemForm();
        form.setName(name);
        form.setDescription(description);
        form.setPrice(new BigDecimal(price));
        form.setCategoryId(categoryId);
        form.setImage(image);
        menuService.createItem(form);
        return "redirect:/admin/items";
    }
    @PostMapping("/items/{id}/delete")
    public String deleteItem(@PathVariable Long id) {
        menuService.deleteItem(id);
        return "redirect:/admin/items";
    }
}
