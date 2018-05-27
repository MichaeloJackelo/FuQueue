package com.storemanagement.dao.order;

import com.storemanagement.entities.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface OrderOwnRepository extends Repository<Order, Long> {
    @Query(value = "from Order")
    List<Order> getAllOrder();

    @Query(value="from Order o where o.id=?1")
    Order getOrderById(long id);

    @Query(value="from Order o where o.status.statusId=?1")
    List<Order> getOrdersByStatus(long status);

    @Query(value="from Order o where o.name like %?1%")
    List<Order> getOrdersByName(String orderName);

    @Query(value="Select distinct name from Order order by name")
    List<String> getAllOrdersName();


    @Modifying
    @Query(value="delete from Order o where o.id=?1")
    void deleteOrder(long orderId);

    @Transactional
    @Modifying
    @Query(value="update Order o Set o.status.statusId=?2 where o.id=?1")
    int changeStatus(long id, long statusId);

    @Transactional
    @Modifying
    @Query(value="update Order o Set o.receiveDate=?1 where o.id=?2")
    int changeReceiveDate(Date date, long orderId);

    @Transactional
    @Modifying
    @Query(value="update Order o Set o.sendDate=?1 where o.id=?2")
    int changeCreatedDate(Date date, long orderId);



}
