package com.salecampaign.salescampaign.repositories;

import com.salecampaign.salescampaign.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepo extends JpaRepository<Admin,Integer> {
    @Query(value = "select admin_id,username,password,role from tbladmin where username = :user",nativeQuery = true)
    List<Object[]> findByUsername(@Param(value = "user") String username);
}
