package com.storemanagement.servicesImpl.order;

import com.storemanagement.dao.order.OrderOwnRepository;
import com.storemanagement.dao.order.OrderRepository;
import com.storemanagement.dao.order.StatusOwnRepository;
import com.storemanagement.dao.order.StatusRepository;
import com.storemanagement.dao.product.ProductOwnRepository;
import com.storemanagement.entities.Order;
import com.storemanagement.entities.OrderProducts;
import com.storemanagement.entities.Product;
import com.storemanagement.entities.Status;
import com.storemanagement.services.order.OrderProductService;
import com.storemanagement.services.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Transactional
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderOwnRepository orderOwnRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private StatusOwnRepository statusOwnRepository;
    @Autowired
    private ProductOwnRepository productOwnRepository;
    @Autowired
    private OrderProductService orderProductService;


    @Override
    public Order getOrderById(long id) {
        return orderOwnRepository.getOrderById(id);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderOwnRepository.getAllOrder();
    }

    @Override
    public Order addOrder(Order order, long statusId) {
        order.setStatus(statusOwnRepository.getStatusById(statusId));
        return orderRepository.save(order);
    }
    public Status getStatusById(long id){
        return statusOwnRepository.getStatusById(id);
    }

    @Override
    public void deleteOrder(long id) {
        orderOwnRepository.deleteOrder(id);
    }

    @Override
    public int changeStatus(long id, long statusId) {
        Date date=new Date(System.currentTimeMillis());
        if(statusId==2){
            orderOwnRepository.changeCreatedDate(date, id);
        }
        if(statusId==3){
            orderOwnRepository.changeReceiveDate(date, id);
            List<Product> products=orderProductService.getAllProductFromOrder(id);
            for(Product product:products){
                productOwnRepository.updateAmount(product.getId(),product.getAmount ()+(int) orderProductService.getOrderProductsById(id, product.getId()).getAmount());
            }

        }
        return orderOwnRepository.changeStatus(id, statusId);
    }

    @Override
    public List<Order> getOrdersByStatus(long status) {
        return orderOwnRepository.getOrdersByStatus(status);
    }

    @Override
    public List<Order> getOrdersByName(String orderName) {
        return orderOwnRepository.getOrdersByName(orderName);
    }

    @Override
    public List<String> getAllOrdersName() {
        return orderOwnRepository.getAllOrdersName();
    }
}
