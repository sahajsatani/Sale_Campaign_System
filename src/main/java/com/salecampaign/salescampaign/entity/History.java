package com.salecampaign.salescampaign.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tblhistory")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class History {

    //Field
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "historyId")
    private int historyId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "beforeDiscountPrice")
    private int beforeDiscountPrice;

    @Column(name = "afterDiscountPrice")
    private int afterDiscountPrice;

    //Mapping
    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

}
