package com.sr.Ziply.service.food;

import com.sr.Ziply.exception.SourceNotFoundException;
import com.sr.Ziply.model.Food;
import com.sr.Ziply.model.Restaurant;
import com.sr.Ziply.request.CreateFoodRequest;
import com.sr.Ziply.response.FoodResponse;

import java.util.List;

public interface FoodService {
    public Food createFood(CreateFoodRequest createFoodRequest, Restaurant restaurant)throws Exception;
    void deleteFood(Long foodId,Restaurant restaurant) throws SourceNotFoundException;
    public List<Food> getRestaurantsFood(Long restaurantId,boolean isVegetarin,boolean isSeasonal,boolean isNonVeg,String foodCategory)throws Exception;
    public List<Food> searchFood(String search)throws Exception;
    public Food findFoodById(Long foodId)throws Exception;

    public Food updateAvailablityStatus(Long foodId) throws SourceNotFoundException;

    FoodResponse convertFoodRes(Food food);
}
