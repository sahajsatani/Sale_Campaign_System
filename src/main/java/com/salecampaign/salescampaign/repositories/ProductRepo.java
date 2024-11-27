package com.salecampaign.salescampaign.repositories;

import com.salecampaign.salescampaign.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, String> {
}
