package com.storemanagement.entities;
import javax.persistence.*;

@Entity
@Table(name="product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "productId")
    private long id;
    @Column(name="name")
    private String name;
    @Column(name="category")
    private String category;
    @Column(name="price")
    private double price;
    @Column(name="provider")
    private String provider;
    @Column(name="amount")
    private int amount;
    @Column(name="maxAmount")
    private int maxAmount;
    @Column(name="img")
    private String img;

    public Product() {
    }
    public Product(Product product){
        this.id=product.getId();
        this.category=product.getCategory();
        this.price=product.getPrice();
        this.provider=product.getProvider();
        this.amount=product.getAmount();
        this.maxAmount=product.getMaxAmount();
        this.name=product.getName();
        this.img=img;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}

