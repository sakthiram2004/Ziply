package com.sr.Ziply.repository;

import com.sr.Ziply.model.Order;
import com.sr.Ziply.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


//    public List<Order> findByCustomer(Long userId);
//    public List<Order> findByRestaurantId(Long restaurantId);
//    public List<Order> findByCustomer(User customer);
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Accept User instead of Long
    public List<Order> findByCustomer(User customer);
    public List<Order> findByRestaurantId(Long restaurantId);
}


