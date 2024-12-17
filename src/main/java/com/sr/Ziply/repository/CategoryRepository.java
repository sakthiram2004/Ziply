package com.sr.Ziply.repository;

import com.sr.Ziply.model.Category;
import com.sr.Ziply.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findByRestaurantId(Long id);

    Category findByNameAndRestaurant(String name, Restaurant restaurant);

//    boolean existsByName(String name);

    Category findByName(String name);

    boolean existsByName(String name);

    boolean existsByNameAndRestaurant(String name, Restaurant restaurant);
}
