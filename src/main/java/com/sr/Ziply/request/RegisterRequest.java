package com.sr.Ziply.request;

import com.sr.Ziply.dto.RestaurantDto;
import com.sr.Ziply.model.Address;
import com.sr.Ziply.model.Order;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String userRole;

}
