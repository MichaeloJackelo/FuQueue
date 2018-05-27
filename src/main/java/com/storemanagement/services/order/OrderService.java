package com.storemanagement.services.order;

import com.storemanagement.entities.Order;
import com.storemanagement.entities.Status;

import java.util.List;

public interface OrderService {
    Order getOrderById(long id);
    List<Order> getAllOrders();
    Order addOrder(Order order, long statusId);
    void deleteOrder(long id);
    int changeStatus(long id, long statusId);
    List<Order> getOrdersByStatus(long status);
    List<Order> getOrdersByName(String orderName);
    List<String> getAllOrdersName();
    Status getStatusById(long id);
}
