package com.storemanagement.servicesImpl.order;

import com.storemanagement.dao.order.OrderProductOwnRepository;
import com.storemanagement.dao.order.OrderProductRepository;
import com.storemanagement.entities.OrderProducts;
import com.storemanagement.entities.Product;
import com.storemanagement.services.order.OrderProductService;
import com.storemanagement.services.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class OrderProductServiceImpl implements OrderProductService {
    @Autowired
    private OrderProductOwnRepository orderProductOwnRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private ProductService productService;

    @Override
    public OrderProducts addProductToOrder(long orderId, long productId, int amount) {
        return orderProductRepository.save(new OrderProducts(orderId,productId,amount));
    }

    @Override
    public List<Product> getAllProductFromOrder(long orderId) {
        List<OrderProducts> orderProducts=orderProductOwnRepository.getAllProductFromOrder(orderId);
        List<Product> products=new ArrayList<Product>();
        for(OrderProducts orderProduct: orderProducts){
            Product product=productService.getProductById(orderProduct.getProductId());
            Product tempProduct=new Product(product);
            tempProduct.setAmount((int) orderProduct.getAmount());
            //Tutaj trzeba pokombinować
            if(product!=null)
                products.add(tempProduct);
        }

        return products;
    }

    @Override
    public void removeProduct(long orderId, long productId) {
        orderProductOwnRepository.removeProduct(orderId, productId);
    }

    @Override
    public void removeAllproducts(long id) {
        orderProductOwnRepository.removeAllproducts(id);
    }

    @Override
    public OrderProducts getOrderProductsById(long orderId, long productId) {
        return orderProductOwnRepository.getOrderProductsById(orderId, productId);
    }
}
