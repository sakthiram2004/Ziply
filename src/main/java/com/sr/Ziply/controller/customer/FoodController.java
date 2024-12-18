package com.sr.Ziply.controller.customer;

import com.sr.Ziply.model.Food;
import com.sr.Ziply.model.User;
import com.sr.Ziply.response.ApiResponse;
import com.sr.Ziply.service.UserService;
import com.sr.Ziply.service.food.FoodService;
import com.sr.Ziply.service.restaurant.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/food")
public class FoodController {
    @Autowired
    private FoodService foodService;
    @Autowired
    private UserService userService;
    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchFood(@RequestParam String name,
                                                 @RequestHeader("Authorization") String jwt)throws Exception{
        try {
            User user = userService.findUserByJwt(jwt.substring(7));
            List<Food> foods = foodService.searchFood(name);
            return ResponseEntity.ok().body( new ApiResponse(" Successfully", foods));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new  ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<ApiResponse> getRestaurantFood(
            @RequestParam boolean vagetarian,

            @RequestParam boolean seasonal,
            @RequestParam boolean nonveg,
            @RequestParam(required
                    =
                    false) String food_category,
            @PathVariable Long restaurantId,
            @RequestHeader("Authorization") String jwt) throws Exception {
        try {
            User user =userService.findUserByJwt(jwt.substring(7));
            List<Food> foods=foodService.getRestaurantsFood (restaurantId, vagetarian, seasonal,nonveg, food_category);
            return ResponseEntity.ok().body( new ApiResponse(" Successfully", foods));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new  ApiResponse(e.getMessage(), null));
        }
    }
}
