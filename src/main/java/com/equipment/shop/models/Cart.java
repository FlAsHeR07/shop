package com.equipment.shop.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Access(AccessType.FIELD)
@MappedSuperclass
public class Cart implements Serializable {
    @Id
    @Column(columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ElementCollection
    @CollectionTable(name = "cart_items", joinColumns = @JoinColumn(name = "cart_id"), foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @MapKeyJoinColumn(name = "good_id")
    @Column(name = "quantity")
    private Map<Good, Integer> cart;

    public Cart() {
        cart = new HashMap<>();
    }

    public Cart(Map<Good, Integer> cart) {
        this.cart = cart;
    }

    public long getId() {
        return id;
    }

    public Map<Good, Integer> getCart() {
        return cart;
    }

    public double calculatePrice() {
        long sum = 0;
        for (Map.Entry<Good, Integer> entry : cart.entrySet()) {
            sum += entry.getKey().getPriceKopeck() * entry.getValue();
        }
        return ((double) sum) / 100;
    }
}
