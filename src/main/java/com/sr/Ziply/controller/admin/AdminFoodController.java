package com.sr.Ziply.controller.admin;

import com.sr.Ziply.exception.SourceNotFoundException;
import com.sr.Ziply.model.Food;
import com.sr.Ziply.model.Restaurant;
import com.sr.Ziply.model.User;
import com.sr.Ziply.request.CreateFoodRequest;
import com.sr.Ziply.response.ApiResponse;
import com.sr.Ziply.service.UserService;
import com.sr.Ziply.service.food.FoodService;
import com.sr.Ziply.service.restaurant.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/food")
public class AdminFoodController {
    @Autowired
    private FoodService foodService;
    @Autowired
    private UserService userService;
    @Autowired
    private RestaurantService restaurantService;
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createFood(@RequestBody CreateFoodRequest req,
                                           @RequestHeader("Authorization") String jwt) throws Exception {
        try {
            User user = userService.findUserByJwt(jwt.substring(7));
            Restaurant restaurant = restaurantService.findRestaurantById(req.getRestaurantId());
            Food food = foodService.createFood(req,restaurant);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("created",foodService.convertFoodRes(food)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new  ApiResponse(e.getMessage(), null));
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteFood(@PathVariable Long id,
                                                  @RequestHeader("Authorization") String jwt) throws Exception {
        try {
            User user = userService.findUserByJwt(jwt.substring(7));
            Restaurant restaurant= restaurantService.getRestaurantByUserId(user.getId());
            foodService.deleteFood(id,restaurant);

            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("DELETED SUCCESSFULLY",null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new  ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/Foodavailablitystatus/{id}")
    public ResponseEntity<ApiResponse> updateFoodAvailablityStatus(@PathVariable Long id,
                                                  @RequestHeader("Authorization") String jwt) throws Exception {
        try {
            User user = userService.findUserByJwt(jwt.substring(7));
            Food food=foodService.updateAvailablityStatus(id);

            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("success",foodService.convertFoodRes(food)));
        } catch (SourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new  ApiResponse(e.getMessage(), null));
        }
    }

}
