package com.sr.Ziply.service.restaurant;

import com.sr.Ziply.dto.RestaurantDto;
import com.sr.Ziply.exception.SourceAlreadyExist;
import com.sr.Ziply.exception.SourceNotFoundException;
import com.sr.Ziply.model.Address;
import com.sr.Ziply.model.Restaurant;
import com.sr.Ziply.model.User;
import com.sr.Ziply.repository.AddressRepository;
import com.sr.Ziply.repository.RestaurantRepository;
import com.sr.Ziply.repository.UserRepository;
import com.sr.Ziply.request.CreateRestaurantRequest;
import com.sr.Ziply.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Restaurant createRestaurant(CreateRestaurantRequest request, User user) throws SourceAlreadyExist {

        if (!restaurantRepository.existsByName(request.getName())) {
            Address address = new Address();
            Address requestAddress = request.getAddress(); // Assuming a single address for simplicity
            address.setStreetAddress(requestAddress.getStreetAddress());
            address.setCity(requestAddress.getCity());
            address.setState(requestAddress.getState());
            address.setPincode(requestAddress.getPincode());
            address.setCountry(requestAddress.getCountry());
            addressRepository.save(address);
            addressRepository.save(address);
            Restaurant restaurant = new Restaurant();
            restaurant.setAddress(address);
            restaurant.setContactInformation(request.getContactInformation());
            restaurant.setCuisineType(request.getCuisionType());
            restaurant.setDescription(request.getDescription());
            restaurant.setImages(request.getImages());
            restaurant.setName(request.getName());
            restaurant.setOpeningHour(request.getOpeningHour());
            restaurant.setRegisterDate(LocalDate.now());
            restaurant.setOwner(user);
            return restaurantRepository.save(restaurant);
        } else {
            throw new SourceAlreadyExist("Source Already exist");
        }
    }

    @Override
    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updateRetaurant) throws SourceNotFoundException {
        if (restaurantRepository.existsById(restaurantId)) {
            Restaurant restaurant = findRestaurantById(restaurantId);
            if (restaurant.getCuisineType() != null) {
                restaurant.setCuisineType(updateRetaurant.getCuisionType());
            }
            Address address = new Address();
            Address requestAddress = updateRetaurant.getAddress(); // Assuming a single address for simplicity
            address.setStreetAddress(requestAddress.getStreetAddress());
            address.setCity(requestAddress.getCity());
            address.setState(requestAddress.getState());
            address.setPincode(requestAddress.getPincode());
            address.setCountry(requestAddress.getCountry());
            addressRepository.save(address);


            restaurant.setAddress(address);
            restaurant.setContactInformation(updateRetaurant.getContactInformation());
            restaurant.setCuisineType(updateRetaurant.getCuisionType());
            restaurant.setDescription(updateRetaurant.getDescription());
            restaurant.setImages(updateRetaurant.getImages());
            restaurant.setName(updateRetaurant.getName());
            restaurant.setOpeningHour(updateRetaurant.getOpeningHour());
            restaurant.setRegisterDate(LocalDate.now());
            return restaurantRepository.save(restaurant);
        } else {
            throw new SourceNotFoundException("Source Not Found");
        }
    }

    @Override
    public void deleteRestaurant(Long restaurantId) throws SourceNotFoundException {
        if (restaurantRepository.existsById(restaurantId)) {
            restaurantRepository.deleteById(restaurantId);
        } else {
            throw new SourceNotFoundException("Source Not Found");
        }

    }

    @Override
    public List<Restaurant> getAllRestaurant() {
        return restaurantRepository.findAll();
    }

    @Override
    public List<Restaurant> searchRestaurant(String search) {
        return restaurantRepository.findBySearchQuery(search);
    }

    @Override
    public Restaurant findRestaurantById(Long id) throws SourceNotFoundException {
        if (restaurantRepository.existsById(id)) {
            Optional<Restaurant> opt = restaurantRepository.findById(id);
            return opt.get();
        } else {
            throw new SourceNotFoundException("Source Not Found");
        }
    }

    @Override
    public Restaurant getRestaurantByUserId(Long userId) {
        Restaurant restaurant = restaurantRepository.findByOwnerId(userId);

        return restaurant;
    }

    @Override
    public RestaurantDto addFavorites(Long restaurantId, User user) throws SourceNotFoundException {
        Restaurant restaurant = findRestaurantById(restaurantId);
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setDescription(restaurant.getDescription());
        restaurantDto.setImages(restaurant.getImages());
        restaurantDto.setTitle(restaurant.getName());
        restaurantDto.setId(restaurantId);

        boolean isFav = false;
        List<RestaurantDto> favorites = user.getFavorites();
        for (RestaurantDto favorite : favorites) {
            if (favorite.getId().equals(restaurantId)) {
//            user.getFavorites().remove(restaurantDto);
                isFav = true;
                break;
            }
        }
        if (isFav) {
            favorites.removeIf(f -> f.getId().equals(restaurantId));
        } else favorites.add(restaurantDto);
        userRepository.save(user);
        return restaurantDto;
    }

    @Override
    public Restaurant updateRestaurantStatus(Long id) throws SourceNotFoundException {

            Restaurant restaurant = findRestaurantById(id);
            restaurant.setOpen(!restaurant.isOpen());
            restaurantRepository.save(restaurant);
            return restaurant;
        }
@Override
        public RestaurantDto convertRestaurantDto(Restaurant restaurant){
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setId(restaurant.getId());
        restaurantDto.setTitle(restaurant.getName());
        restaurantDto.setDescription(restaurant.getDescription());
        restaurantDto.setImages(restaurant.getImages());
        restaurantDto.setOpen(restaurant.isOpen());
        return restaurantDto;
        }

}