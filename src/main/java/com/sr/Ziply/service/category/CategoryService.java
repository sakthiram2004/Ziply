package com.sr.Ziply.service.category;

import com.sr.Ziply.model.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(String name, Long id);

    Category findCategoryById(Long id);

    List<Category> findCategoryByRestaurantId(Long id);
}
