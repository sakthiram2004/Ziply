package com.sr.Ziply.request;

import com.sr.Ziply.model.Category;
import com.sr.Ziply.model.IngredientsItem;
import lombok.Data;

import java.util.List;
@Data

public class CreateFoodRequest {
    private String  name;
    private String description;
    private Long price;
    private Category category;
    private List<String> images;
    private Long restaurantId;
    private boolean isvegetarin;
    private boolean seasional;
//    private List<IngredientsItem> ingredients;
}
