package com.sr.Ziply.repository;

import com.sr.Ziply.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food,Long> {
    List<Food> findByRestaurantId(Long restaurantId);

//    @Query("SELECT f FROM Food f WHERE f.name LIKE %:search% OR f.foodCategory.name LIKE %:search%")
//    List<Food> searchFood(@Param("search") String search);

    @Query("SELECT f FROM Food f " +
            "WHERE f.name LIKE %:search% " +
            "OR (f.category IS NOT NULL AND f.category.name LIKE %:search%)")
    List<Food> searchFood(@Param("search") String search);


    Optional<Food> findByNameAndRestaurantId(String name, Long id);
}
