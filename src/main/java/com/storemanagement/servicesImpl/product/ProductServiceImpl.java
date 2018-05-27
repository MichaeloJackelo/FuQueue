package com.storemanagement.servicesImpl.product;

import com.storemanagement.dao.product.ProductOwnRepository;
import com.storemanagement.dao.product.ProductRepository;
import com.storemanagement.entities.Product;
import com.storemanagement.services.product.ProductService;
import com.storemanagement.util.HibernateUtilConfig;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Transactional
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductOwnRepository productOwnRepository;

    @Autowired
    private HibernateUtilConfig hb;

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Product> getAllProducts() {
        return productOwnRepository.getAllProducts();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productOwnRepository.getProductsByCategory(category);
    }

    @Override
    public List<Product> getProductsByProvider(String provider) {
        return productOwnRepository.getProductsByProvider(provider);
    }

    @Override
    public List<Product> getProductByName(String name, String category, String provider) {
        if(category==null)
            category="";
        if(provider==null)
            provider="";
        if(name==null)
            name="";

        return productOwnRepository.getProductByName(name, category, provider);
    }

    @Override
    public List<String> getAllCategories() {
        return productOwnRepository.getAllCategories();
    }

    @Override
    public List<String> getAllProviders() {
        return productOwnRepository.getAllproviders();
    }

    @Override
    public Product getProductById(long id) {
        return productOwnRepository.getProductById(id);
    }

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public int updateProduct(long id, Product product) {
        if(hb!=null){
            product.setId(id);
            System.out.println("Name: "+product.getName());
            System.out.println("Id: "+ product.getId());

            Session session=hb.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(product);
            session.getTransaction().commit();
            session.close();

            System.out.println("is not null");
        }
        else{
            System.out.println("is null");
        }
        //return productOwnRepository.updateProduct(id, product);
        return 1;
    }

    @Override
    public void removeProduct(long id) {
        productRepository.delete(id);
    }
}
