package com.sr.Ziply.controller.owner;

import com.sr.Ziply.exception.SourceAlreadyExist;
import com.sr.Ziply.exception.SourceNotFoundException;
import com.sr.Ziply.model.Order;
import com.sr.Ziply.model.Restaurant;
import com.sr.Ziply.model.User;
import com.sr.Ziply.request.CreateRestaurantRequest;
import com.sr.Ziply.response.ApiResponse;
import com.sr.Ziply.service.UserService;
import com.sr.Ziply.service.order.OrderService;
import com.sr.Ziply.service.restaurant.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owner/restaurants")
public class OwnerRestaurantController {

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createRestaurent(@RequestBody CreateRestaurantRequest req, @RequestHeader("Authorization") String jwt){

        User user = userService.findUserByJwt(jwt.substring(7));

        try {
            Restaurant restaurant = restaurantService.createRestaurant(req,user);

            return  ResponseEntity.ok().body(new ApiResponse("Created Successfully",restaurant));
        } catch (SourceAlreadyExist e) {
            return ResponseEntity.badRequest().body(new  ApiResponse(e.getMessage(), null));
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateRestaurent(@RequestBody CreateRestaurantRequest req, @RequestHeader("Authorization") String jwt,@PathVariable Long id){
        User user = userService.findUserByJwt(jwt.substring(7));

        try {
            Restaurant restaurant = restaurantService.updateRestaurant(id,req);

            return  ResponseEntity.ok().body(new ApiResponse("Update Successfully",restaurant));
        } catch (SourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new  ApiResponse(e.getMessage(), null));
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteRestaurent(@PathVariable Long id){

        try {
            restaurantService.deleteRestaurant(id);

            return  ResponseEntity.ok().body(new ApiResponse("Deleted  Successfully",null));
        } catch (SourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new  ApiResponse(e.getMessage(), null));
        }
    }
    @PutMapping("/update/status/{id}")
    public ResponseEntity<ApiResponse> updateRestaurentStatus( @RequestHeader("Authorization") String jwt,@PathVariable Long id){
        User user = userService.findUserByJwt(jwt.substring(7));

        try {
            Restaurant restaurant = restaurantService.updateRestaurantStatus(id);

            return  ResponseEntity.ok().body(new ApiResponse("Update Restaurant Status Successfully",restaurant));
        } catch (SourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new  ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/user")

    public ResponseEntity<ApiResponse> findUserById( @RequestHeader("Authorization") String jwt){
        User user = userService.findUserByJwt(jwt.substring(7));
        Restaurant restaurant = restaurantService.getRestaurantByUserId(user.getId());
        return  ResponseEntity.ok().body(new ApiResponse("Restaurant find  Successfully",restaurant));
    }
    @PutMapping("/update/orderstatus/{id}")
    public ResponseEntity<ApiResponse> updateOrderStatus(@PathVariable Long id,@RequestParam String status){


        try {
            Order order = orderService.updateOrder(id,status);

            return  ResponseEntity.ok().body(new ApiResponse("Update Restaurant Status Successfully",order));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new  ApiResponse(e.getMessage(), null));
        }
    }

}
