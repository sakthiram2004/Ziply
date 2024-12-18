package com.sr.Ziply.response;

import com.sr.Ziply.model.Address;
import com.sr.Ziply.model.ContactInformation;
import com.sr.Ziply.model.User;
import jakarta.persistence.Embedded;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
public class RestaurentResponse {
    private Long id;
    private String name;
    private String description;
    private String cuisineType;
    private Address address;
    private ContactInformation contactInformation;
    private String openingHour;
    private String owner;
}
