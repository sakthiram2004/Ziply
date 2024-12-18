package com.sr.Ziply.service.food;

import com.sr.Ziply.exception.SourceAlreadyExist;
import com.sr.Ziply.exception.SourceNotFoundException;
import com.sr.Ziply.model.Category;
import com.sr.Ziply.model.Food;
import com.sr.Ziply.model.Restaurant;
import com.sr.Ziply.repository.CategoryRepository;
import com.sr.Ziply.repository.FoodRepository;
import com.sr.Ziply.repository.RestaurantRepository;
import com.sr.Ziply.request.CreateFoodRequest;
import com.sr.Ziply.response.FoodResponse;
import com.sr.Ziply.service.category.CategoryService;
import com.sr.Ziply.service.restaurant.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService{
@Autowired
    private FoodRepository foodRepository;
@Autowired
private CategoryService categoryService;
@Autowired
private RestaurantRepository restaurantRepository;
@Autowired
private CategoryRepository categoryRepository;
@Autowired
private RestaurantService restaurantService;
   @Override
    public Food createFood(CreateFoodRequest createFoodRequest, Restaurant restaurant)throws Exception {
       Optional<Food> existingFood = foodRepository.findByNameAndRestaurantId(
               createFoodRequest.getName(), restaurant.getId());

       if (existingFood.isPresent()) {
           throw new SourceAlreadyExist("Food with the same name already exists in the restaurant.");
       }


       Category category = createCategory(createFoodRequest.getCategory().getName(), restaurant.getId());

       Food food = new Food();
       food.setCategory(category);
       food.setRestaurant(restaurant);
       food.setDescription(createFoodRequest.getDescription());
       food.setImages(createFoodRequest.getImages());
       food.setName(createFoodRequest.getName());
       food.setPrice(createFoodRequest.getPrice());
       // food.setIngredientsItems(createFoodRequest.getIngredients());
       food.setSeasonal(createFoodRequest.isSeasional());
       food.setVegetarion(createFoodRequest.isIsvegetarin());

       // Save the new Food item
       Food savedFood = foodRepository.save(food);

       // Add the saved food item to the restaurant
       restaurant.getFoods().add(savedFood);

       return savedFood;
    }

    @Override
    public void deleteFood(Long foodId,Restaurant restaurant) throws SourceNotFoundException {
        try {
            List<Food> foods = restaurant.getFoods();
            if(foods.contains(foodRepository.findById(foodId))){
                foods.remove(foodRepository.findById(foodId));
                foodRepository.deleteById(foodRepository.findById(foodId).get().getId());
            }
            restaurantRepository.save(restaurant);
        } catch (Exception e) {
            throw new SourceNotFoundException("Source Not Found");
        }

    }

    @Override
    public List<Food> getRestaurantsFood(Long restaurantId, boolean isVegetarin, boolean isSeasonal, boolean isNonVeg,String foodCategory) {
        List<Food> foods = foodRepository.findByRestaurantId(restaurantId);
        if(isVegetarin){
            foods = filterByVegetarian(foods,isVegetarin);
        }
        if(!isVegetarin){
            foods = filterByNonVeg(foods,isNonVeg);
        }
        if(isSeasonal){
            foods = filterBySeasnol(foods,isSeasonal);

        }
        if(foodCategory!=null && !foodCategory.equals("")){
            foods = filterByCategery(foods,foodCategory);
        }
        return foods;
    }

    private List<Food> filterByCategery(List<Food> foods, String foodCategory) {

        return foods.stream().filter(food->
        {
            if (food.getCategory()!=null){
                return food.getCategory().getName().equals(foodCategory);
            }
            return false;
        }).collect(Collectors.toList());

    }

    private List<Food> filterBySeasnol(List<Food> foods, boolean isSeasonal) {
        return foods.stream().filter(food->food.isSeasonal()== isSeasonal).collect(Collectors.toList());
    }

    private List<Food> filterByNonVeg(List<Food> foods, boolean b) {
        return foods.stream().filter(food->food.isVegetarion()== false).collect(Collectors.toList());
    }

    private List<Food> filterByVegetarian(List<Food> foods, boolean isVegetarin) {
        return foods.stream().filter(food->food.isVegetarion()== isVegetarin).collect(Collectors.toList());
    }

    @Override
    public List<Food> searchFood(String search) {
        return foodRepository.searchFood(search);
    }

    @Override
    public Food findFoodById(Long foodId) throws SourceNotFoundException {
        Optional<Food> optionalFood=foodRepository.findById(foodId);
        if (optionalFood.isEmpty()){
           throw  new SourceNotFoundException("food not exist...");
        }
        return optionalFood.get();
    }

    @Override
    public Food updateAvailablityStatus(Long foodId) throws SourceNotFoundException {
        Food food = findFoodById(foodId);
        food.setAvailable(!food.isAvailable());

        return foodRepository.save(food);
    }

    public Category createCategory(String name, Long restaurantId) {
        try {
            Restaurant restaurant = restaurantService.getRestaurantByUserId(restaurantId);

            // Check if the category already exists for this restaurant
            Category existingCategory = categoryRepository.findByNameAndRestaurant(name, restaurant);

            // If the category exists, return the existing one
            if (existingCategory != null) {
                return existingCategory;
            }

            // If not, create a new category
            Category category = new Category();
            category.setName(name);
            category.setRestaurant(restaurant);

            // Save and return the new category
            return categoryRepository.save(category);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public boolean existsByNameAndRestaurant(String name, Restaurant restaurant) {
        return categoryRepository.existsByNameAndRestaurant(name, restaurant);
    }

    @Override
    public FoodResponse convertFoodRes(Food food){
       FoodResponse foodResponse = new FoodResponse();
       foodResponse.setId(food.getId());
       foodResponse.setName(food.getName());
       foodResponse.setAvailable(food.isAvailable());
       foodResponse.setDescription(food.getDescription());
       foodResponse.setCategory(food.getCategory().getName());
       foodResponse.setImages(food.getImages());
       foodResponse.setPrice(food.getPrice());
       foodResponse.setRestaurant(food.getRestaurant().getName());
       foodResponse.setSeasonal(food.isSeasonal());
       foodResponse.setVegetarion(food.isVegetarion());
       foodResponse.setCreationDate(new Date());
       return foodResponse;
    }


}
