package com.salecampaign.amazon.repositories;

import com.salecampaign.amazon.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepos extends JpaRepository<Campaign, Integer> {
    @Query(value = "select * from tblcampaign where start_date=current_date()", nativeQuery = true)
    List<Object[]> getCampaignStartByCid();

    @Query(value = "select * from tblcampaign where end_date=current_date()", nativeQuery = true)
    List<Object[]> getCampaignStopByCid();
}
