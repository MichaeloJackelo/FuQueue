package com.storemanagement.dao.product;

import com.storemanagement.entities.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductOwnRepository extends Repository<Product, Long> {

    @Query(value = "from Product")
    List<Product> getAllProducts();

    @Query(value = "from Product p where p.id=?1")
    Product getProductById(long id);

    @Query(value="from Product p where p.category=?1")
    List<Product> getProductsByCategory(String category);

    @Query(value="from Product p where p.provider=?1")
    List<Product> getProductsByProvider(String provider);

    @Query(value="from Product p where p.name like %?1% and p.category like %?2% and p.provider like %?3%")
    List<Product> getProductByName(String name, String category, String provider);

    @Query(value="Select distinct category from Product order by category")
    List<String> getAllCategories();

    @Query(value="Select distinct provider from Product order by provider")
    List<String> getAllproviders();

    @Transactional
    @Modifying
    @Query(value="update Product p Set p=?2 where p.id=?1")
    int updateProduct(long id, Product product);

    @Transactional
    @Modifying
    @Query(value="update Product p Set p.amount=?2 where p.id=?1")
    void updateAmount(long id, int amount);
}
