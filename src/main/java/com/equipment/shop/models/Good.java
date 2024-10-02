package com.equipment.shop.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Base64;
import java.util.List;

@Access(AccessType.PROPERTY)
@Entity
public class Good implements Serializable {
    @Access(AccessType.FIELD)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private long priceKopeck;
    private String description;
    private String category;
    private byte[] image;
    private List<Manufacturer> manufacturers;
    private int quantity;

    public Good() {
    }

    public Good(String name, long priceKopeck, String description, String category, byte[] image, List<Manufacturer> manufacturers) {
        this.name = name;
        this.priceKopeck = priceKopeck;
        this.description = description;
        this.category = category;
        this.image = image;
        this.manufacturers = manufacturers;
    }

    public long getId() {
        return id;
    }

    @Column(columnDefinition = "text")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPriceKopeck() {
        return priceKopeck;
    }

    public void setPriceKopeck(long priceKopeck) {
        this.priceKopeck = priceKopeck;
    }

    @Transient
    public double getPriceGrn() {
        return (double) (priceKopeck) / 100;
    }

    @Column(columnDefinition = "text")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Transient
    public String getImageBase64() {
        return Base64.getEncoder().encodeToString(image);
    }


    @ManyToMany
    @JoinTable(
        name = "good_manufacturer",
        joinColumns = @JoinColumn(name = "good_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "manufacturer_id", referencedColumnName = "id")
    )
    public List<Manufacturer> getManufacturers() {
        return manufacturers;
    }

    public void setManufacturers(List<Manufacturer> manufacturers) {
        this.manufacturers = manufacturers;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
