package org.example.coffeeshop.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.List;
@Setter
@Getter
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String language;
    @Column(name = "image_filename")
    private String imageFilename;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<MenuItem> items;

}
