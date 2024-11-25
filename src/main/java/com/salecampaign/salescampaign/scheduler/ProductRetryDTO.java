package com.salecampaign.salescampaign.scheduler;

import lombok.Data;

@Data
public class ProductRetryDTO {
    String productId;
    int discount;
    public ProductRetryDTO(String productId, int discount) {
        this.productId = productId;
        this.discount = discount;
    }
}
