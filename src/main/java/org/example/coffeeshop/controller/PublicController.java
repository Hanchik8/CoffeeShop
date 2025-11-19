package org.example.coffeeshop.controller;
import org.example.coffeeshop.model.Category;
import org.example.coffeeshop.model.MenuItem;
import org.example.coffeeshop.repository.CategoryRepository;
import org.example.coffeeshop.repository.MenuItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
@Controller
@RequestMapping
public class PublicController {
    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;
    public PublicController(CategoryRepository categoryRepository, MenuItemRepository menuItemRepository) {
        this.categoryRepository = categoryRepository;
        this.menuItemRepository = menuItemRepository;
    }
    @GetMapping("/")
    public String index() {
        return "index";
    }
    @GetMapping("/{lang}/categories")
    public String categories(@PathVariable String lang, Model model) {
        List<Category> categories = categoryRepository.findByLanguageOrderByNameAsc(lang);
        model.addAttribute("categories", categories);
        model.addAttribute("lang", lang);
        return "categories";
    }
    @GetMapping("/{lang}/category/{id}/items")
    public String items(@PathVariable String lang, @PathVariable Long id, Model model) {
        List<MenuItem> items = menuItemRepository.findByCategoryIdOrderByNameAsc(id);
        model.addAttribute("items", items);
        model.addAttribute("lang", lang);
        return "items";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
