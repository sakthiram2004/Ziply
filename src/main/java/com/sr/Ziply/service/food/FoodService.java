package com.sr.Ziply.service.food;

import com.sr.Ziply.exception.SourceNotFoundException;
import com.sr.Ziply.model.Food;
import com.sr.Ziply.model.Restaurant;
import com.sr.Ziply.request.CreateFoodRequest;

import java.util.List;

public interface FoodService {
    public Food createFood(CreateFoodRequest createFoodRequest, Restaurant restaurant);
    void deleteFood(Long foodId,Restaurant restaurant) throws SourceNotFoundException;
    public List<Food> getRestaurantsFood(Long restaurantId,boolean isVegetarin,boolean isSeasonal,boolean isNonVeg,String foodCategory);
    public List<Food> searchFood(String search);
    public Food findFoodById(Long foodId)throws Exception;

    public Food updateAvailablityStatus(Long foodId) throws SourceNotFoundException;
}
