package com.salecampaign.salescampaign.repositories;

import com.salecampaign.salescampaign.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerRepo extends JpaRepository<Seller,Integer> {
    @Query(value = "select seller_id,username,password from tblseller where username = :user",nativeQuery = true)
    List<Object[]> findByUsername(@Param(value = "user") String username);
}
