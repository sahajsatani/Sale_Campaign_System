package com.salecampaign.amazon.sheduler;

import lombok.Data;

@Data
public class ProductRetryInfo {
    String productId;
    int discount;

    public ProductRetryInfo(String productId, int discount) {
        this.productId = productId;
        this.discount = discount;
    }
}
