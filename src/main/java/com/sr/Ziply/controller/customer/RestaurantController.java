package com.sr.Ziply.controller.customer;

import com.sr.Ziply.dto.RestaurantDto;
import com.sr.Ziply.exception.SourceNotFoundException;
import com.sr.Ziply.model.Restaurant;
import com.sr.Ziply.model.User;
import com.sr.Ziply.response.ApiResponse;
import com.sr.Ziply.service.UserService;
import com.sr.Ziply.service.restaurant.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/restaurants")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchRestaurent(@RequestHeader("Authorization") String jwt,
                                                        @RequestParam String search) {
        User user = userService.findUserByJwt(jwt.substring(7));
        List<Restaurant> restaurant = restaurantService.searchRestaurant(search);
        return ResponseEntity.ok().body(new ApiResponse("Found Successfully", restaurant));
    }

    @GetMapping("/getAllRestaurants")
    public ResponseEntity<ApiResponse> getAllRestaurant(@RequestHeader("Authorization") String jwt) {

//            User user = userService.findUserByJwt(jwt);
            List<Restaurant> restaurant = restaurantService.getAllRestaurant();
            return ResponseEntity.ok().body(new ApiResponse(" Successfully", restaurant));



    }

    @GetMapping("/getrestaurant/{id}")
    public ResponseEntity<ApiResponse> findRestaurentById(@PathVariable Long id) {
        try {
            Restaurant restaurant = restaurantService.findRestaurantById(id);

        return ResponseEntity.ok().body(new ApiResponse("Found Successfully", restaurant));
        } catch (SourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}/add-favorite")
    public ResponseEntity<ApiResponse> addFavorite(@RequestHeader("Authorization") String jwt, @PathVariable Long id){
    User user = userService.findUserByJwt(jwt.substring(7));

        try {
            RestaurantDto   restaurantDto = restaurantService.addFavorites(id,user);

        return  ResponseEntity.ok().body(new ApiResponse("Found Successfully",restaurantDto));
        } catch (SourceNotFoundException e) {
            throw new RuntimeException(e);
        }
}

}


