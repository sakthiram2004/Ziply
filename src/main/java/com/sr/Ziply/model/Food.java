package com.sr.Ziply.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;
    private Long price;

    @ManyToOne
    private Category category;

    @Column(length = 1000)
    @ElementCollection
    private List<String> images;

    private boolean available;
    @ManyToOne
    private Restaurant restaurant;
    private boolean isVegetarion;
    private boolean isSeasonal;

//    @ManyToMany
//    private List<IngredientsItem> ingredientsItems;

    private Date creationDate;
}
