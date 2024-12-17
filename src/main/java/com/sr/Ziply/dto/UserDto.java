package com.sr.Ziply.dto;

import com.sr.Ziply.enums.UserRole;
import com.sr.Ziply.model.Address;
import com.sr.Ziply.model.Order;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private UserRole role;
    private List<Order> orders;

    private List<RestaurantDto> favorites;

    private List<Address> addresses;
}
