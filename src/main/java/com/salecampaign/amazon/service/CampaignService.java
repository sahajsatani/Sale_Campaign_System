package com.salecampaign.amazon.service;

import com.salecampaign.amazon.model.Campaign;
import com.salecampaign.amazon.model.Discount;
import com.salecampaign.amazon.repositories.CampaignRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampaignService {
    @Autowired
    CampaignRepos campaignRepo;

    public ResponseEntity<?> addCampaign(List<Campaign> list) {
        try {
            int count = 0;
            for (Campaign i : list) {
                List<Discount> discountsList = i.getDiscounts();
                for (Discount j : discountsList) {
                    j.setCampaign(i);
                }
                campaignRepo.save(i);
                count++;
            }
            if (count > 0)
                return new ResponseEntity<>("Saved campaign", HttpStatus.ACCEPTED);
            else
                return new ResponseEntity<>("Not Saved campaign", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
