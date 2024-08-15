package com.salecampaign.amazon.repositories;

import com.salecampaign.amazon.model.Discount;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepo extends JpaRepository<Discount, Integer> {
    @Query(value = "select discount,product_id from tbldiscount d where d.campaign_id = ?1", nativeQuery = true)
    List<Object[]> getDiscountsByCampId(int cid);

    @Modifying
    @Query(value = "delete from tbldiscount where product_id = :id",nativeQuery = true)
    public void deleteAllByProduct(@Param(value = "id") String list);

}
