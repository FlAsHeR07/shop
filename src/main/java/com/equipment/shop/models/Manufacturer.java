package com.equipment.shop.models;

import jakarta.persistence.*;

import java.util.List;

@Access(AccessType.PROPERTY)
@Entity
public class Manufacturer {
    @Access(AccessType.FIELD)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String contacts;
    private String description;
    private List<Good> goods;

    Good good;

    public Manufacturer() {
    }

    public Manufacturer(String name, String contacts, String description) {
        this.name = name;
        this.contacts = contacts;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(columnDefinition = "text")
    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    @Column(columnDefinition = "text")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToMany(mappedBy = "manufacturers")
    public List<Good> getGoods() {
        return goods;
    }

    public void setGoods(List<Good> goods) {
        this.goods = goods;
    }
}
