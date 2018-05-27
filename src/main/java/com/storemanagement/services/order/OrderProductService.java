package com.storemanagement.services.order;

import com.storemanagement.entities.OrderProducts;
import com.storemanagement.entities.Product;

import java.util.List;

public interface OrderProductService {
    OrderProducts addProductToOrder(long orderId, long productId, int amount);
    List<Product> getAllProductFromOrder(long orderId);
    void removeProduct(long orderId, long productId);
    void removeAllproducts(long id);
    OrderProducts getOrderProductsById(long orderId, long productId);
}
