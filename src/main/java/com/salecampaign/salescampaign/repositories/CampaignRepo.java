package com.salecampaign.salescampaign.repositories;

import com.salecampaign.salescampaign.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepo extends JpaRepository<Campaign, Integer> {
    @Query(value = "select * from tblcampaign where start_date=current_date()", nativeQuery = true)
    List<Object[]> getCampaignStartByCid();

    @Query(value = "select * from tblcampaign where end_date=current_date()", nativeQuery = true)
    List<Object[]> getCampaignStopByCid();
}
