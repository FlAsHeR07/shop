package com.equipment.shop.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Access(AccessType.FIELD)
@Entity
public class Cart implements Serializable {
    @Id
    @Column(columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "cart_items", joinColumns = @JoinColumn(name = "cart_id"), foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @MapKeyJoinColumn(name = "good_id")
    @Column(name = "quantity")
    private Map<Good, Integer> items;

    public Cart() {
        items = new HashMap<>();
    }

    public Cart(Map<Good, Integer> items) {
        this.items = items;
    }

    public Cart(Cart items) {
        this.items = new HashMap<>(items.getItems());
    }

    public long getId() {
        return id;
    }

    public Map<Good, Integer> getItems() {
        return items;
    }

    public double calculatePrice() {
        long sum = 0;
        for (Map.Entry<Good, Integer> entry : items.entrySet()) {
            sum += entry.getKey().getPriceKopeck() * entry.getValue();
        }
        return ((double) sum) / 100;
    }
}
