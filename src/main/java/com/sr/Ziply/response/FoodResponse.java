package com.sr.Ziply.response;

import com.sr.Ziply.model.Category;
import com.sr.Ziply.model.Restaurant;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data

public class FoodResponse {
    private Long id;

    private String name;
    private String description;
    private Long price;

    private String category;
    private List<String> images;

    private boolean available;
    private String restaurant;
    private boolean isVegetarion;
    private boolean isSeasonal;

    private Date creationDate;
}
