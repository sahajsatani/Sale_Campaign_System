package com.salecampaign.amazon.service;

import com.salecampaign.amazon.model.Campaign;
import com.salecampaign.amazon.model.Discount;
import com.salecampaign.amazon.repositories.CampaignRepos;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CampaignService {
    @Autowired
    CampaignRepos campaignRepo;

//    @Async
    @Transactional
    public ResponseEntity<?> addCampaign(List<Campaign> list) {
        if(list.size()==0){
            throw new NoSuchElementException("Not have any list of campaign");
        }
        try {
            int count = 0;
            //add current campaign into discount of list
            for (Campaign i : list) {
                if(i.getStartDate().isAfter(LocalDate.now()) && i.getEndDate().isAfter(i.getStartDate())) {
                    List<Discount> discountsList = i.getDiscounts();
                    discountsList.forEach(j -> {
                        j.setCampaign(i);
                    });
                    campaignRepo.save(i);
                    count++;
                }
            }

            //check how many data will inserted
            if (count>0)
                return new ResponseEntity<>(count+" campaign saved", HttpStatus.ACCEPTED);
            else
                return new ResponseEntity<>("Not saved any campaign", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
