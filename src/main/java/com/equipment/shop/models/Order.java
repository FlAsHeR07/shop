package com.equipment.shop.models;


import jakarta.persistence.*;

import java.util.Date;
import java.util.Map;

@Access(AccessType.PROPERTY)
@Entity
@Table(name = "\"order\"")
public class Order extends Cart{
    @Access(AccessType.FIELD)
    @Id
    @Column(columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private User user;
    private Date timeOfPayment;

    public Order() {
    }

    public Order(Map<Good, Integer> cart, User user, Date timeOfPayment) {
        super(cart);
        this.user = user;
        this.timeOfPayment = timeOfPayment;
    }

    public long getId() {
        return id;
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Temporal(value = TemporalType.DATE)
    public Date getTimeOfPayment() {
        return timeOfPayment;
    }

    public void setTimeOfPayment(Date timeOfPayment) {
        this.timeOfPayment = timeOfPayment;
    }
}
