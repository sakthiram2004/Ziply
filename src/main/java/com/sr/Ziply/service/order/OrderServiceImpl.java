package com.sr.Ziply.service.order;

import com.sr.Ziply.model.*;

import com.sr.Ziply.repository.*;
import com.sr.Ziply.request.OrderRequest;
import com.sr.Ziply.service.cart.CartService;
import com.sr.Ziply.service.restaurant.RestaurantService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class OrderServiceImpl implements OrderService {
    @Autowired

    private OrderRepository orderRepository;
    @Autowired
    private OrderItemsRepository orderItemsRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartService cartService;
//    @Override
//    public Order createOrder(OrderRequest order, User user) throws Exception {
//        Address shippedAddress = order.getDeliveryAddress();
//        Address saveAddress = addressRepository.save(shippedAddress);
//        if(!user.getAddresses().contains(saveAddress)){
//            user.getAddresses().add(saveAddress);
//            userRepository.save(user);
//        }
//        Restaurant restaurant = restaurantService.findRestaurantById(order.getRestaurantId());
//        Order createOrder = new Order();
//        createOrder.setCustomer(user);
//        createOrder.setCreatedAt(new Date());
//        createOrder.setOrderStatus("PENDING");
//        createOrder.setDeliveryAddress(saveAddress);
//        createOrder.setRestaurant(restaurant);
//
//        Cart cart = cartService.findCartByUserId(user.getId());
//
//        List<OrderItems> orderItemsList = new ArrayList<>();
//
//        for(CartItems cartItems :cart.getCartItems()){
//            OrderItems orderItems = new OrderItems();
//            orderItems.setFood(cartItems.getFood());
//            orderItems.setQuantity(cartItems.getQuantity());
//            orderItems.setTotalPrice(cartItems.getTotalPrice());
//            OrderItems savedItems = orderItemsRepository.save(orderItems);
//            orderItemsList.add(orderItems);
//
//        }
//        Long totalprice = cartService.calculateCartTotal(cart);
//        createOrder.setOrderItemsList(orderItemsList);
//        createOrder.setTotalPrice(totalprice);
//
//        Order saveOrder = orderRepository.save(createOrder);
//        restaurant.getOrderList().add(saveOrder);
//        return createOrder;
//    }
@Override
@Transactional
public Order createOrder(OrderRequest orderRequest, User user) throws Exception {

    // Step 1: Save the delivery address and associate with the user
    Address deliveryAddress = orderRequest.getDeliveryAddress();
    Address savedAddress = addressRepository.save(deliveryAddress);
    if (!user.getAddresses().contains(savedAddress)) {
        user.getAddresses().add(savedAddress);
        userRepository.save(user);
    }

    // Step 2: Find the restaurant and associate order
    Restaurant restaurant = restaurantService.findRestaurantById(orderRequest.getRestaurantId());
    if (restaurant == null) {
        throw new Exception("Restaurant not found.");
    }

    // Step 3: Create the order
    Order newOrder = new Order();
    newOrder.setCustomer(user);
    newOrder.setCreatedAt(new Date());
    newOrder.setOrderStatus("PENDING");
    newOrder.setDeliveryAddress(savedAddress);
    newOrder.setRestaurant(restaurant);

    // Step 4: Process cart items and calculate total price
    Cart cart = cartService.findCartByUserId(user.getId());
    if (cart == null || cart.getCartItems().isEmpty()) {
        throw new Exception("Cart is empty.");
    }

    List<OrderItems> orderItemsList = new ArrayList<>();
    for (CartItems cartItem : cart.getCartItems()) {
        OrderItems orderItem = new OrderItems();
        orderItem.setFood(cartItem.getFood());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setTotalPrice(cartItem.getTotalPrice());
        orderItemsRepository.save(orderItem);
        orderItemsList.add(orderItem);
    }

    newOrder.setOrderItemsList(orderItemsList);
    newOrder.setTotalPrice(cartService.calculateCartTotal(cart));

    // Step 5: Save the order
    Order savedOrder = orderRepository.save(newOrder);

    // Step 6: Add the order to the restaurant owner's orders
    User restaurantOwner = restaurant.getOwner();
    System.out.println(restaurantOwner.getName());
    if (restaurantOwner != null) {
        // Initialize the orders list for the restaurant owner if null
        if (restaurantOwner.getOrders() == null) {
            restaurantOwner.setOrders(new ArrayList<>());
        }
        restaurantOwner.getOrders().add(savedOrder);
        userRepository.save(restaurantOwner); // Persist the owner with the new order
    }

    // Step 7: Save the restaurant with the new order (if necessary)
    if (restaurant.getOrderList() == null) {
        restaurant.setOrderList(new ArrayList<>());
    }
    restaurant.getOrderList().add(savedOrder);
    restaurantRepository.save(restaurant);

    return savedOrder;
}


    @Override
    public Order updateOrder(Long orderId, String orderStatus) throws Exception {
        Order order = findOrderById(orderId);
        if (orderStatus.equals("OUT_FOR_DELIVERY")
        || orderStatus.equals("DELIVERY")
            || orderStatus.equals("COMPLETED")
                || orderStatus.equals("PENDING")){
            order.setOrderStatus(orderStatus);

        }
        return orderRepository.save(order);
    }

    @Override
    public void calculateOrder(Long orderId) throws Exception {
        Order order = findOrderById(orderId);
        orderRepository.deleteById(orderId);

    }

    @Override
    public List<Order> getUserOrder(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));
        return orderRepository.findByCustomer(user);
    }

    @Override
    public List<Order> getRestaurentOrder(Long restaurantId, String orderStatus) throws Exception {
        List<Order> orders = orderRepository.findByRestaurantId(restaurantId);
        if (orderStatus!=null){
            orders = orders.stream().filter(f->
                f.getOrderStatus().equals(orderStatus)).collect(Collectors.toList());

        }
        return orders;
    }

    @Override
    public Order findOrderById(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        return optionalOrder.get();

    }
}
