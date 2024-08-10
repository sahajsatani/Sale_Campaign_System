package com.salecampaign.amazon.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tblproduct")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @Column(name = "productid")
    private String productId;

    @Column(name = "titel")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "mrp")
    private int mrp;

    @Column(name = "currentprice")
    private int currentPrice;

    @Column(name = "discount")
    private int discount;

    @Column(name = "inventorycount")
    private int inventoryCount;


    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<Discount> discounts = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<History> histories = new ArrayList<>();

    public Product(String productId) {
        this.productId = productId;
    }
}