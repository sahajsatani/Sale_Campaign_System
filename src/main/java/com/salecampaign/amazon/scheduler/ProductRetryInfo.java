package com.salecampaign.amazon.scheduler;

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
