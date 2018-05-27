package com.storemanagement.controllers.orders;

import com.storemanagement.entities.Order;
import com.storemanagement.entities.OrderProducts;
import com.storemanagement.entities.Product;
import com.storemanagement.entities.Status;
import com.storemanagement.services.order.OrderProductService;
import com.storemanagement.services.order.OrderService;
import com.storemanagement.services.order.StatusService;
import com.storemanagement.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;

@RestController
@PreAuthorize("hasAnyRole('ADMIN')")
@RequestMapping("/secured/orders")
public class OrdersRestController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private OrderProductService orderProductService;


    @RequestMapping(method=RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public List<Order> getAllOrders(@RequestParam(value="name", required = false) String orderName){
        if(orderName!=null && !orderName.equals("")){
            return orderService.getOrdersByName(orderName);
        }
        return orderService.getAllOrders();
    }
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public Order getOrderById(@PathVariable long id){
        return orderService.getOrderById(id);
    }

    @RequestMapping(value="/name", method=RequestMethod.GET)
    public List<String> getAllOrdersName(){
        return orderService.getAllOrdersName();
    }

    @RequestMapping(method=RequestMethod.POST)
    public Order addOrder(@RequestBody Order order, HttpSession session){
        long status=1;
        long userId=userService.getUserByName(session.getAttribute("username").toString()).getId();
        order.setAuthorId(userId);
        return orderService.addOrder(order,status);
    }

    @RequestMapping(value="/{id}/status/{statusName}",method=RequestMethod.PUT)
    public void changeStatus(@PathVariable long id, @PathVariable String statusName){
        long statusId=statusService.getStatusByName(statusName).getStatusId();
        if(statusId<4){
            statusId++;
            orderService.changeStatus(id, statusId);
        }

    }
    @RequestMapping(value="/status/{status}")
    public List<Order> getOrdersByStatus(@PathVariable long status){
        return orderService.getOrdersByStatus(status);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public void deleteOrder(@PathVariable long id){
        orderService.deleteOrder(id);
        orderProductService.removeAllproducts(id);
    }


    @RequestMapping(value="{name}/products/{productId}/amount/{amount}", method=RequestMethod.POST)
    public OrderProducts addProductToOrder(@PathVariable String name, @PathVariable long productId, @PathVariable int amount){
        long orderId=orderService.getOrdersByName(name).get(0).getId();
        return orderProductService.addProductToOrder(orderId, productId,amount);
    }

    @RequestMapping(value="/{id}/products", method=RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Product> getAllProductsFromOrder(@PathVariable long id){
        return orderProductService.getAllProductFromOrder(id);
    }

    @RequestMapping(value="/{orderId}/products/{productId}", method=RequestMethod.DELETE)
    public void deleteProductFromOrder(@PathVariable long orderId, @PathVariable long productId){
        orderProductService.removeProduct(orderId, productId);
    }
}
