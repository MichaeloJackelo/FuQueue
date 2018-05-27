package com.storemanagement.controllers.products;

import com.storemanagement.entities.Product;
import com.storemanagement.services.product.ProductService;
import com.storemanagement.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@PreAuthorize("hasAnyRole('ADMIN')")
@RequestMapping("/secured/products")
public class ProductsRestController {
    @Autowired
    private ProductService productService;
    /*GET Methods*/



    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public Product getProductById(@PathVariable long id){
        return productService.getProductById(id);
    }

    @RequestMapping(method=RequestMethod.POST)
    public Product addProduct(@RequestBody Product product){
        return productService.addProduct(product);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public int updateProduct(@PathVariable long id, @RequestBody Product product){
        return productService.updateProduct(id, product);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public void removeProduct(@PathVariable long id){
        productService.removeProduct(id);
    }

    @RequestMapping(value="/categories", method=RequestMethod.GET)
    public List<String> getAllCategories(){
        return productService.getAllCategories();
    }

    @RequestMapping(value="/providers", method = RequestMethod.GET)
    public List<String> getAllProviders(){
        return productService.getAllProviders();
    }

    @RequestMapping(value="/categories/{category}", method=RequestMethod.GET)
    public List<Product> getProductByCategory(@PathVariable String category){
        return productService.getProductsByCategory(category);
    }

    @RequestMapping(value="/providers/{provider}", method = RequestMethod.GET)
    public List<Product> getProductByProvider(@PathVariable String provider){
        return productService.getProductsByProvider(provider);
    }


}
