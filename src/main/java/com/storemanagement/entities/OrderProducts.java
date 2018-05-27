package com.storemanagement.entities;

import javax.persistence.*;

@Entity
@Table(name="order_product")
public class OrderProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "row_id")
    private long id;
    @Column(name="order_id")
    private long orderId;
    @Column(name="product_id")
    private long productId;
    @Column(name="amount")
    private long amount;

    public OrderProducts() {
    }

    public OrderProducts(long orderId, long productId, long amount) {
        this.orderId = orderId;
        this.productId = productId;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
