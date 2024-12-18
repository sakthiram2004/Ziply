package com.sr.Ziply.service.category;

import com.sr.Ziply.exception.SourceNotFoundException;
import com.sr.Ziply.model.Category;
import com.sr.Ziply.model.Restaurant;
import com.sr.Ziply.repository.CategoryRepository;
import com.sr.Ziply.service.restaurant.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImp implements CategoryService{
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public Category createCategory(String name, Long id)throws Exception{
        try {
            Restaurant restaurant = restaurantService.getRestaurantByUserId(id);
            Category category = new Category();
            category.setName(name);
            category.setRestaurant(restaurant);
            return categoryRepository.save(category);
        } catch (Exception e) {
            throw new SourceNotFoundException("Source not found");
        }

    }

    @Override
    public Category findCategoryById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        return optionalCategory.get();
    }

    @Override

    public List<Category> findCategoryByRestaurantId(Long id)throws Exception{
        Restaurant restaurant = restaurantService.getRestaurantByUserId(id);
        return categoryRepository.findByRestaurantId(restaurant.getId());
    }


}
