package com.sr.Ziply.response;

import com.sr.Ziply.model.CartItems;
import lombok.Data;

import java.util.List;
@Data
public class CartResponse {
    private Long cartId;
    private String CustomerName;
    private Long cartTotal;
    private List<CartItems> cartItems;
}
