package org.example.coffeeshop.repository;

import org.example.coffeeshop.model.Category;
import org.example.coffeeshop.model.MenuItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class MenuItemRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Test
    @DisplayName("findByCategoryIdOrderByNameAsc should return items for the category")
    void findByCategoryId_shouldReturnItems() {
        Category category = new Category();
        category.setName("Coffee");
        category.setLanguage("ru");
        category = categoryRepository.save(category);

        MenuItem item = new MenuItem();
        item.setCategory(category);
        item.setName("Latte");
        item.setPrice(new BigDecimal("150.00"));
        menuItemRepository.save(item);

        List<MenuItem> items = menuItemRepository.findByCategoryIdOrderByNameAsc(category.getId());

        assertThat(items).hasSize(1);
        assertThat(items.get(0).getName()).isEqualTo("Latte");
        assertThat(menuItemRepository.existsByCategoryId(category.getId())).isTrue();
    }

    @Test
    @DisplayName("findAllWithCategory should fetch category data")
    void findAllWithCategory_shouldFetchCategory() {
        Category category = new Category();
        category.setName("Coffee");
        category.setLanguage("ru");
        category = categoryRepository.save(category);

        MenuItem item = new MenuItem();
        item.setCategory(category);
        item.setName("Cappuccino");
        item.setPrice(new BigDecimal("170.00"));
        menuItemRepository.save(item);

        List<MenuItem> items = menuItemRepository.findAllWithCategory();

        assertThat(items).hasSize(1);
        assertThat(items.get(0).getCategory()).isNotNull();
    }
}
