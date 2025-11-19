package org.example.coffeeshop.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Setter
@Getter
@Entity
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(columnDefinition = "text")
    private String description;
    private BigDecimal price;
    @Column(name = "image_filename")
    private String imageFilename;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
