package com.storemanagement.dao.order;

import com.storemanagement.entities.Order;
import com.storemanagement.entities.OrderProducts;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface OrderProductOwnRepository extends Repository<OrderProducts, Long> {

    @Query("from OrderProducts op where op.orderId=?1" )
    List<OrderProducts> getAllProductFromOrder(long orderId);

    @Modifying
    @Query(value="delete from OrderProducts op where op.orderId=?1 and op.productId=?2")
    void removeProduct(long orderId, long productId);

    @Modifying
    @Query(value="delete from OrderProducts op where op.orderId=?1")
    void removeAllproducts(long id);

    @Query("from OrderProducts op where op.orderId=?1 and op.productId=?2")
    OrderProducts getOrderProductsById(long orderId, long productId);
}
