package org.example.coffeeshop.repository;
import org.example.coffeeshop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByLanguageOrderByNameAsc(String language);
}
