package com.storemanagement.controllers.products;

import com.storemanagement.entities.Product;
import com.storemanagement.services.product.ProductService;
import com.storemanagement.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@PreAuthorize("hasAnyRole('ADMIN')")
@RequestMapping("/secured/products")
public class ProductsController {

    @Autowired
    private ProductService productService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping
    public String menu(HttpSession session){
        session.setAttribute("username", SecurityUtils.getUserName());
        return "menu";
    }

    @RequestMapping(method=RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Product> getAllProducts(@RequestParam(value="name", required = false) String productName,
                                        @RequestParam(value="category", required = false) String category,
                                        @RequestParam(value="provider", required = false) String provider){

        return productService.getProductByName(productName, category, provider);
    }
}
