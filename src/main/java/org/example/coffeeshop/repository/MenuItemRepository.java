package org.example.coffeeshop.repository;
import org.example.coffeeshop.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByCategoryIdOrderByNameAsc(Long categoryId);
}
