package com.sr.Ziply.service.cart;

import com.sr.Ziply.exception.SourceNotFoundException;
import com.sr.Ziply.model.Cart;
import com.sr.Ziply.model.CartItems;
import com.sr.Ziply.model.Food;
import com.sr.Ziply.model.User;
import com.sr.Ziply.repository.CartItemsRepository;
import com.sr.Ziply.repository.CartRepository;
import com.sr.Ziply.request.AddCartItemRequest;
import com.sr.Ziply.response.AddCartResponse;
import com.sr.Ziply.response.CartResponse;
import com.sr.Ziply.service.UserService;
import com.sr.Ziply.service.food.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    private UserService userService;
    @Autowired
    private FoodService foodService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemsRepository cartItemsRepository;
    @Override
//    public CartItems addItemToCart(AddCartItemRequest req, String jwt) throws Exception {
//        User user = userService.findUserByJwt(jwt.substring(7));
//        Food food = foodService.findFoodById(req.getFoodId());
//        Cart cart = cartRepository.findByCustomerId(user.getId());
//        if (cart == null) {
//            cart = new Cart();
//            cart.setCustomer(user);
//            cart.setTotal(0L);
//
//        }
//        else {
//
//
//
//        for (CartItems cartItems : cart.getCartItems()){
//            if (cartItems.getFood().equals(food)){
//                int newquantity = cartItems.getQuantity()+req.getQuantity();
//                return updateCartItemQuantity(cartItems.getId(),newquantity);
//            }
//        }}
//
//        CartItems newCartItems = new CartItems();
//        newCartItems.setCart(cart);
//        newCartItems.setFood(food);
//        newCartItems.setQuantity(req.getQuantity());
//        newCartItems.setTotalPrice(req.getQuantity()*food.getPrice());
//
//        CartItems saveCartItem = cartItemsRepository.save(newCartItems);
//        cart.getCartItems().add(saveCartItem);
//        cartRepository.save(cart);
//        return saveCartItem;
//    }
    public CartItems addItemToCart(AddCartItemRequest req, String jwt) throws Exception {
        User user = userService.findUserByJwt(jwt.substring(7));
        Food food = foodService.findFoodById(req.getFoodId());

        // Check if the cart exists
        Cart cart = cartRepository.findByCustomerId(user.getId());
        if (cart == null) {
            cart = new Cart();
            cart.setCustomer(user);
            cart.setTotal(0L);

            // Save the cart to make it persistent
            cart = cartRepository.save(cart);
        }

        // Check if the food item already exists in the cart
        for (CartItems cartItems : cart.getCartItems()) {
            if (cartItems.getFood().equals(food)) {
                int newQuantity = cartItems.getQuantity() + req.getQuantity();
                return updateCartItemQuantity(cartItems.getId(), newQuantity);
            }
        }

        // Add a new cart item
        CartItems newCartItems = new CartItems();
        newCartItems.setCart(cart); // Cart is now persistent
        newCartItems.setFood(food);
        newCartItems.setQuantity(req.getQuantity());
        newCartItems.setTotalPrice(req.getQuantity() * food.getPrice());

        CartItems savedCartItem = cartItemsRepository.save(newCartItems);

        // Update cart's item list and total
        cart.getCartItems().add(savedCartItem);
        cart.setTotal(calculateCartTotal(cart));
        cartRepository.save(cart); // Update cart

        return savedCartItem;
    }


    @Override
    public CartItems updateCartItemQuantity(Long cartItemId, int quantity) throws Exception {
        try {
            Optional<CartItems> cartItemsOptional=cartItemsRepository.findById(cartItemId);
            CartItems items = cartItemsOptional.get();
            items.setQuantity(quantity);
            items.setTotalPrice(items.getFood().getPrice()*quantity);
            Cart cart = items.getCart();


            cart.setTotal(calculateCartTotal(cart));
            cartRepository.save(cart);

            return        cartItemsRepository.save(items);
        } catch (Exception e) {
            throw new SourceNotFoundException("CartItemId Not Found");
        }

    }

    @Override
    public Cart removeCartItemFromCart(Long cartItemId, String jwt) throws Exception {
        User user = userService.findUserByJwt(jwt.substring(7));
        Cart cart = cartRepository.findByCustomerId(user.getId());
        Optional<CartItems> cartItemsOptional= cartItemsRepository.findById(cartItemId);

        CartItems items = cartItemsOptional.get();
        cart.getCartItems().remove(items);
        cart =  cartRepository.save(cart);
        cart.setTotal(calculateCartTotal(cart));
        return cartRepository.save(cart);

    }

    @Override
    public Long calculateCartTotal(Cart cart) throws Exception {
//        Long total = 0L;
//        for (CartItems cartItems:cart.getCartItems()){
//            total+= cartItems.getFood().getPrice()*cartItems.getQuantity();
//        }
        Long cartTotal = 0L;
        for (CartItems cartItems : cart.getCartItems()) {
            cartTotal+=cartItems.getTotalPrice();
        }
        return cartTotal;
    }

    @Override
    public Cart findCartById(Long id) throws Exception {
        try {
            Optional<Cart> cart = cartRepository.findById(id);
            return cart.get();
        } catch (Exception e) {
            throw new SourceNotFoundException("Cart id Not Found");
        }
    }

    @Override
    public Cart findCartByUserId(String jwt) throws Exception {
        User user = userService.findUserByJwt(jwt.substring(7));
         return cartRepository.findByCustomerId(user.getId());

    }

    @Override
    public Cart findCartByUserId(Long id) throws Exception {
        try {
            return cartRepository.findByCustomerId(id);
        } catch (Exception e) {
            throw new SourceNotFoundException("User Not Found");
        }
    }

    @Override
    public Cart clearCart(String jwt) throws Exception {
        User user = userService.findUserByJwt(jwt.substring(7));
        Cart cart = findCartByUserId(jwt);

        cart.getCartItems().clear();
        cart.setTotal(0L);
        return cartRepository.save(cart);
    }

    public CartResponse convertCartResponse(Cart cart){
        CartResponse cartResponse = new CartResponse();
        cartResponse.setCartId(cart.getId());
        cartResponse.setCartTotal(cart.getTotal());
        cartResponse.setCustomerName(cart.getCustomer().getName());
        cartResponse.setCartItems(cart.getCartItems());
        return cartResponse;
    }
@Override
    public AddCartResponse addCartResponse(CartItems cartItems){
        AddCartResponse addCartResponse = new AddCartResponse();
        addCartResponse.setCartItenId(cartItems.getId());
        addCartResponse.setImages(cartItems.getFood().getImages());
        addCartResponse.setQuantity(cartItems.getQuantity());
        addCartResponse.setTotalPrice(cartItems.getTotalPrice());
        addCartResponse.setFoodName(cartItems.getFood().getName());
        addCartResponse.setCategoryName(cartItems.getFood().getCategory().getName());
        addCartResponse.setFoodPrice(cartItems.getFood().getPrice());
        addCartResponse.setRestaurentName(cartItems.getFood().getRestaurant().getName());
        return addCartResponse;
    }
}
