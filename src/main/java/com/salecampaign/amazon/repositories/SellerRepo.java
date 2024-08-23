package com.salecampaign.amazon.repositories;

import com.salecampaign.amazon.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepo extends JpaRepository<Seller,Integer> {
}
