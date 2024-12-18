package com.sr.Ziply.controller.common;

import com.sr.Ziply.model.Cart;
import com.sr.Ziply.model.CartItems;
import com.sr.Ziply.request.AddCartItemRequest;
import com.sr.Ziply.request.UpdateCartItemRequest;
import com.sr.Ziply.response.ApiResponse;
import com.sr.Ziply.service.cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;


    @PostMapping("/add/cart-item")
    public ResponseEntity<ApiResponse> addItemsToCart(@RequestBody AddCartItemRequest request,
                                                      @RequestHeader("Authorization") String jwt)throws Exception{
        try {
            CartItems cartItems = cartService.addItemToCart(request,jwt);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Added ",cartService.addCartResponse(cartItems)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new  ApiResponse(e.getMessage(), null));
        }


    }
    @PutMapping("/update/cart-item/quantity")

    public ResponseEntity<ApiResponse> updateCartItemQuantity(@RequestBody UpdateCartItemRequest request,
                                                              @RequestHeader("Authorization") String jwt)throws Exception{
        try {
            CartItems cartItems = cartService.updateCartItemQuantity(request.getCartItemId(),request.getQuantity());
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("updated ",cartService.addCartResponse(cartItems)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new  ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/cart-item/{id}/remove")
    public ResponseEntity<ApiResponse> remoeCartItem(@PathVariable Long id,
                                                     @RequestHeader("Authorization") String jwt)throws Exception{
        try {
            Cart cart = cartService.removeCartItemFromCart(id,jwt);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("removed ",cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new  ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/cart/clear")
    public ResponseEntity<ApiResponse> clearCart(
                                                     @RequestHeader("Authorization") String jwt)throws Exception{
        try {
            Cart cart = cartService.clearCart(jwt);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("clear ",cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new  ApiResponse(e.getMessage(), null));
        }
    }
    @GetMapping("/cart")
    public ResponseEntity<ApiResponse> findUserCart(
            @RequestHeader("Authorization") String jwt)throws Exception{
        try {
            Cart cart = cartService.findCartByUserId(jwt);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("success ",cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new  ApiResponse(e.getMessage(), null));
        }
    }

}
