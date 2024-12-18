package com.sr.Ziply.service.cart;

import com.sr.Ziply.model.Cart;
import com.sr.Ziply.model.CartItems;
import com.sr.Ziply.request.AddCartItemRequest;
import com.sr.Ziply.response.AddCartResponse;

public interface CartService {

    public CartItems addItemToCart(AddCartItemRequest req, String jwt)throws Exception;
    public CartItems updateCartItemQuantity(Long cartItemId,int quantity)throws Exception;

    public Cart removeCartItemFromCart(Long cartItemId,String jwt)throws Exception;
    public Long calculateCartTotal(Cart cart)throws Exception;
    public Cart findCartById(Long id)throws Exception;



    Cart findCartByUserId(String jwt) throws Exception;
    Cart findCartByUserId(Long id) throws Exception;

    Cart clearCart(String jwt) throws Exception;

    AddCartResponse addCartResponse(CartItems cartItems);
}
