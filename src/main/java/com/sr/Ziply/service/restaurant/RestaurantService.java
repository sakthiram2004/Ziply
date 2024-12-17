package com.sr.Ziply.service.restaurant;

import com.sr.Ziply.dto.RestaurantDto;
import com.sr.Ziply.exception.SourceAlreadyExist;
import com.sr.Ziply.exception.SourceNotFoundException;
import com.sr.Ziply.model.Restaurant;
import com.sr.Ziply.model.User;
import com.sr.Ziply.request.CreateRestaurantRequest;

import java.util.List;

public interface RestaurantService {
    public Restaurant createRestaurant(CreateRestaurantRequest request, User user) throws SourceAlreadyExist;
    public Restaurant updateRestaurant(Long restaurantId,CreateRestaurantRequest updateRetaurant) throws SourceNotFoundException;
    public void deleteRestaurant(Long restaurantId) throws SourceNotFoundException;
    public List<Restaurant> getAllRestaurant();


    List<Restaurant> searchRestaurant(String search);

    public Restaurant findRestaurantById(Long id) throws SourceNotFoundException;
    public Restaurant getRestaurantByUserId(Long userId);
    public RestaurantDto addFavorites(Long restaurantId,User user) throws SourceNotFoundException;
    public Restaurant updateRestaurantStatus(Long id) throws SourceNotFoundException;

    RestaurantDto convertRestaurantDto(Restaurant restaurant);
}
