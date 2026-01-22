package org.example.coffeeshop.controller;

import org.example.coffeeshop.model.Category;
import org.example.coffeeshop.model.MenuItem;
import org.example.coffeeshop.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
@Controller
@RequestMapping
public class PublicController {
    private final MenuService menuService;

    public PublicController(MenuService menuService) {
        this.menuService = menuService;
    }
    @GetMapping("/")
    public String index() {
        return "index";
    }
    @GetMapping("/{lang}/categories")
    public String categories(@PathVariable String lang, Model model) {
        String safeLang = normalizeLanguage(lang);
        if (!safeLang.equals(lang)) {
            return "redirect:/" + safeLang + "/categories";
        }
        List<Category> categories = menuService.getCategoriesByLanguage(safeLang);
        model.addAttribute("categories", categories);
        model.addAttribute("lang", safeLang);
        return "categories";
    }
    @GetMapping("/{lang}/category/{id}/items")
    public String items(@PathVariable String lang, @PathVariable Long id, Model model) {
        String safeLang = normalizeLanguage(lang);
        if (!safeLang.equals(lang)) {
            return "redirect:/" + safeLang + "/category/" + id + "/items";
        }
        Optional<Category> category = menuService.getCategory(id);
        if (category.isEmpty()) {
            return "redirect:/" + safeLang + "/categories";
        }
        List<MenuItem> items = menuService.getItemsByCategory(id);
        model.addAttribute("items", items);
        model.addAttribute("lang", safeLang);
        return "items";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    private String normalizeLanguage(String lang) {
        if (lang == null) {
            return "ru";
        }
        String normalized = lang.trim().toLowerCase();
        return ("en".equals(normalized) ? "en" : "ru");
    }

}
