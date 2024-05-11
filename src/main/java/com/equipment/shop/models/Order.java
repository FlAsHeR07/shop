package com.equipment.shop.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class Order implements Serializable {
    private int order_id;
    private Date timeOfPayment;
    private double price;
    private Map<Integer, Integer> cart;
    private Map<String, Integer> cartForView;

    public Order(int order_id, Date timeOfPayment, double price, Map<Integer, Integer> cart) {
        this.order_id = order_id;
        this.timeOfPayment = timeOfPayment;
        this.price = price;
        this.cart = cart;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public Date getTimeOfPayment() {
        return timeOfPayment;
    }

    public void setTimeOfPayment(Date timeOfPayment) {
        this.timeOfPayment = timeOfPayment;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Map<Integer, Integer> getCart() {
        return cart;
    }

    public void setCart(Map<Integer, Integer> cart) {
        this.cart = cart;
    }

    public Map<String, Integer> getCartForView() {
        return cartForView;
    }

    public void setCartForView(Map<String, Integer> cartForView) {
        this.cartForView = cartForView;
    }
}
