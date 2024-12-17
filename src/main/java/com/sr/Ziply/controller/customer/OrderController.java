package com.sr.Ziply.controller.customer;

import com.sr.Ziply.model.Order;
import com.sr.Ziply.model.User;
import com.sr.Ziply.request.OrderRequest;
import com.sr.Ziply.response.ApiResponse;
import com.sr.Ziply.service.UserService;
import com.sr.Ziply.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createOrder(@RequestBody OrderRequest request,@RequestHeader("Authorization") String jwt)throws Exception{
        User user = userService.findUserByJwt(jwt.substring(7));
        Order order = orderService.createOrder(request,user);
        return ResponseEntity.ok().body( new ApiResponse(" Successfully", order.getOrderItemsList()));
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse> getOrderHistory(@RequestHeader("Authorization") String jwt)throws Exception{
        User user = userService.findUserByJwt(jwt.substring(7));
        List<Order> order = orderService.getUserOrder(user.getId());
        return ResponseEntity.ok().body( new ApiResponse(" Successfully", order));
    }
}
