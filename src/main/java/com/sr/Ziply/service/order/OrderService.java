package com.sr.Ziply.service.order;

import com.sr.Ziply.model.Order;
import com.sr.Ziply.model.User;
import com.sr.Ziply.request.OrderRequest;

import java.util.List;

public interface OrderService {
    public Order createOrder(OrderRequest order, User user) throws Exception;
    public Order updateOrder(Long orderId,String orderStatus)throws Exception;
    public void calculateOrder(Long orderId)throws Exception;
    public List<Order> getUserOrder(Long userId)throws Exception;
    public List<Order> getRestaurentOrder(Long restaurantId,String orderStatus)throws Exception;
    public Order findOrderById(Long orderId);
}
