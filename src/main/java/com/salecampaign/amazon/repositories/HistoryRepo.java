package com.salecampaign.amazon.repositories;

import com.salecampaign.amazon.model.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepo extends JpaRepository<History, Integer> {
}
