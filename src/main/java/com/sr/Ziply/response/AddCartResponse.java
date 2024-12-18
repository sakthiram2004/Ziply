package com.sr.Ziply.response;

import lombok.Data;

import java.util.List;

@Data
public class AddCartResponse {
private Long cartItenId;
private String foodName;
private Long foodPrice;
private String categoryName;
private List<String> images;
//private boolean avilable;
private String restaurentName;
private int quantity;
private Long totalPrice;
}
