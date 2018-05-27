package com.storemanagement.entities;

import javax.persistence.*;

@Entity
@Table(name="status")
public class Status {
    @Id
    @Column(name="status_id")
    private long statusId;
    @Column(name="name")
    private String name;


    public Status(Status status) {
        this.statusId = status.getStatusId();
        this.name = status.getName();
    }
    public Status(){

    }

    public long getStatusId() {
        return statusId;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
