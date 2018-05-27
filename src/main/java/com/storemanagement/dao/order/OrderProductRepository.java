package com.storemanagement.dao.order;

import com.storemanagement.entities.OrderProducts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProducts, Long> {
}
