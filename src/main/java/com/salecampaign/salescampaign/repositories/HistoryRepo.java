package com.salecampaign.salescampaign.repositories;

import com.salecampaign.salescampaign.model.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepo extends JpaRepository<History, Integer> {
}
