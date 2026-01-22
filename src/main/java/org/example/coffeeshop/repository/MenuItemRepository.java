package org.example.coffeeshop.repository;
import org.example.coffeeshop.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByCategoryIdOrderByNameAsc(Long categoryId);

    @Query("select distinct i from MenuItem i left join fetch i.category")
    List<MenuItem> findAllWithCategory();

    boolean existsByCategoryId(Long categoryId);
}
