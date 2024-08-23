package com.salecampaign.amazon.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tblseller")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int sellerId;

    private String username;

    private String password;

    private String productCategory;


    //Mapping
    @OneToMany(mappedBy = "seller",cascade = CascadeType.ALL)
    private List<Product> products;

    private int totalProduct;
}
