package com.sr.Ziply.request;

import com.sr.Ziply.model.Address;
import com.sr.Ziply.model.Order;
import com.sr.Ziply.model.User;
import lombok.Data;

@Data
public class OrderRequest {
    private Long restaurantId;

    private Address deliveryAddress;

}
