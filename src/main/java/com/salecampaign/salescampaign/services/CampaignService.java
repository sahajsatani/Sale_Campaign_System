package com.salecampaign.salescampaign.services;

import com.salecampaign.salescampaign.model.Campaign;
import com.salecampaign.salescampaign.model.Discount;
import com.salecampaign.salescampaign.model.enums.CampaignStatus;
import com.salecampaign.salescampaign.repositories.CampaignRepo;
import com.salecampaign.salescampaign.repositories.DiscountRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CampaignService {
    @Autowired
    CampaignRepo campaignRepo;
    @Autowired
    DiscountRepo discountRepo;

//     @Async
    @Transactional
    public ResponseEntity<?> addCampaign(List<Campaign> list) {
        if (list.isEmpty()) {
            throw new NoSuchElementException("Not have any list of campaign");
        }
        try {
            int count = 0;
            List<Campaign> campaigns = new ArrayList<>();
            //add current campaign into discount of list
            for (Campaign i : list) {
                System.out.println(i.getStartDate());
                System.out.println(i.getEndDate());
                if (i.getStartDate().isAfter(LocalDate.now()) && i.getEndDate().isAfter(i.getStartDate())) {
                    List<Discount> discountsList = i.getDiscounts();
                    discountsList.forEach(j -> {
                        j.setCampaign(i);
                    });
                    i.setDiscounts(discountsList);
                    campaigns.add(i);
                    count++;
                }
            }
            //check how many data was inserted
            if (!campaignRepo.saveAll(campaigns).isEmpty() && count > 0)
                return new ResponseEntity<>(count + " campaign saved", HttpStatus.ACCEPTED);
            else
                return new ResponseEntity<>("Not saved any campaign", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> updateCampaign(Campaign campaign) {
        try {
            Campaign campaign1 = campaignRepo.findById(campaign.getCampaignId()).get();
            if (campaign1 != null) {

                if (campaign1.getStatus() == CampaignStatus.UPCOMING) {
                    if (campaign.getStartDate().isAfter(LocalDate.now()) && campaign.getEndDate().isAfter(campaign.getStartDate())) {
                        campaign.setDiscounts(null);
                        campaignRepo.save(campaign);
                        return new ResponseEntity<>("Campaign updated.", HttpStatus.OK);
                    }
                    return new ResponseEntity<>("Campaign not changed.", HttpStatus.OK);
                }
                else if (campaign1.getStatus() == CampaignStatus.CURRENT) {
                    if (campaign.getEndDate().isAfter(LocalDate.now())) {
                        campaign.setDiscounts(null);
                        campaign1.setNormalEndDate(String.valueOf(campaign.getEndDate()));
                        campaign1.setTitle(campaign.getTitle());
                        campaignRepo.save(campaign1);
                        return new ResponseEntity<>("Campaign updated.", HttpStatus.OK);
                    }
                    return new ResponseEntity<>("Campaign not changed.", HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<>("Campaign was expired.", HttpStatus.OK);
                }

            }
            else {
                return new ResponseEntity<>("Not updated campaign.", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
