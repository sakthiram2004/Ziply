package com.sr.Ziply.request;

import com.sr.Ziply.model.*;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateRestaurantRequest {
    private Long id;
    private String name;
    private String description;
    private String cuisionType;
    private Address address;
    private ContactInformation contactInformation;
    private String openingHour;
    private List<String> images;
    //    private LocalDate registerDate;
    //    private boolean open;
    //    private List<Food> foods;

}
