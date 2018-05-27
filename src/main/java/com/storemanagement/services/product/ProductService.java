package com.storemanagement.services.product;

import com.storemanagement.entities.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByProvider(String provider);
    List<Product> getProductByName(String name, String category, String provider);
    List<String> getAllCategories();
    List<String> getAllProviders();

    Product getProductById(long id);

    Product addProduct(Product product);

    int updateProduct(long id, Product product);

    void removeProduct(long id);



}
