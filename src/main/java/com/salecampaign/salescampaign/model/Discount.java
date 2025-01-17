package com.salecampaign.salescampaign.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbldiscount")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int discountId;
    @Column(nullable = false)
    private int discount;

    //Mapping
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;
}
