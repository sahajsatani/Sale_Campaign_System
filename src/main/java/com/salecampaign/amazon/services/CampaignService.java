package com.salecampaign.amazon.services;

import com.salecampaign.amazon.entity.Campaign;
import com.salecampaign.amazon.entity.Discount;
import com.salecampaign.amazon.repositories.CampaignRepo;
import com.salecampaign.amazon.repositories.DiscountRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class CampaignService {
    @Autowired
    CampaignRepo campaignRepo;

    @Autowired
    DiscountRepo discountRepo;

    //    @Async
    @Transactional
    public ResponseEntity<?> addCampaign(List<Campaign> list) {
        if (list.size() == 0) {
            throw new NoSuchElementException("Not have any list of campaign");
        }
        try {
            int count = 0;
            //add current campaign into discount of list
            for (Campaign i : list) {
                if (i.getStartDate().isAfter(LocalDate.now()) && i.getEndDate().isAfter(i.getStartDate())) {
                    List<Discount> discountsList = i.getDiscounts();
                    discountsList.forEach(j -> {
                        j.setCampaign(i);
                    });
                    campaignRepo.save(i);
                    count++;
                }
            }

            //check how many data will inserted
            if (count > 0) return new ResponseEntity<>(count + " campaign saved", HttpStatus.ACCEPTED);
            else return new ResponseEntity<>("Not saved any campaign", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> updateCampaign(List<Campaign> list) {
        try{
            StringBuilder st = new StringBuilder("Campaign updated");
            List<Campaign> list1 = list.stream().map((campaign -> {
                Campaign campaign1 = campaignRepo.findById(campaign.getCampaignId()).get();
                if (campaign1 != null) {
                    if (campaign1.getStartDate().isBefore(LocalDate.now())
                            || campaign.getStartDate().isBefore(LocalDate.now())
                            || campaign.getStartDate().isAfter(campaign.getEndDate())
                            || campaign.getStartDate()==campaign.getEndDate()
                            || Objects.equals(campaign.getStartDate(), LocalDate.now())) {
                        campaign.setStartDate(campaign1.getStartDate().toString());
                        st.append(" start date was not updated");
                    }
                    if (campaign.getEndDate().isBefore(LocalDate.now())
                        || campaign.getEndDate().isBefore(campaign.getStartDate())
                    ) {
                        campaign.setEndDate(campaign1.getEndDate().toString());
                        st.append(" end date was not updated");
                    }else{
                        campaign.setEndDate(campaign.getEndDate().plusDays(0).toString());
                    }
                    campaign.setDiscounts(null);
                    return campaign;
                } else return null;
            })).toList();

            if (null != campaignRepo.saveAll(list1)) {
                return new ResponseEntity<>(st, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Not updated campaign.", HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
