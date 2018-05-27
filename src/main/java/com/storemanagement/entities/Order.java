package com.storemanagement.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private long id;
    @Column(name="send_date")
    private Timestamp sendDate;
    @Column(name="predicted_date")
    private Timestamp predictedDate;
    @Column(name="receive_date")
    private Timestamp receiveDate;
    @Column(name="author_id")
    private long authorId;
    @Column(name="name")
    private String name;
    @ManyToOne(cascade=CascadeType.ALL)
    private Status status;

    public Order(Order order) {

        this.id = order.getId();
        this.sendDate=order.getSendDate();
        this.receiveDate=order.getReceiveDate();
        this.predictedDate=order.getPredictedDate();
        this.authorId = order.getAuthorId();
        this.name = order.getName();

        System.out.println("Order constructor");
    }


    public Order() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getSendDate() {
        return sendDate;
    }

    public void setSendDate(Timestamp sendDate) {
        this.sendDate = sendDate;
    }

    public Timestamp getPredictedDate() {
        return predictedDate;
    }

    public void setPredictedDate(Timestamp predictedDate) {
        this.predictedDate = predictedDate;
    }

    public Timestamp getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Timestamp receiveDate) {
        this.receiveDate = receiveDate;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        System.out.println("Set status");
        this.status = status;
    }
}
