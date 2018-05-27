package com.storemanagement.controllers.orders;

import com.storemanagement.services.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("hasAnyRole('ADMIN')")
@RequestMapping("/secured/orders")
public class OrdersController {
    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping
    public String orders(){
        return "orders";
    }

}
