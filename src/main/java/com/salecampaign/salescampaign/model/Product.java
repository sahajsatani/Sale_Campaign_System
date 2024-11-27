package com.salecampaign.salescampaign.model;

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

    //field
    @Id
    @Column(name = "productid")
    private String productId;
    @Column(name = "title",nullable = false)
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "mrp",nullable = false)
    private int mrp;
    @Column(name = "currentprice",nullable = false)
    private int currentPrice;
    @Column(name = "discount")
    private int discount;
    @Column(name = "inventorycount",nullable = false)
    private int inventoryCount;

    //Mapping
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<Discount> discounts = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<History> histories = new ArrayList<>();
    public Product(String productId) {
        this.productId = productId;
    }
}