package com.salecampaign.amazon.controller;

import com.salecampaign.amazon.model.Campaign;
import com.salecampaign.amazon.service.CampaignService;
import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campaign")
public class CampaignControl {

    @Autowired
    CampaignService campaignService;

    //add list of campaign
    @PostMapping("/add")
    public ResponseEntity<?> addCampaign(@RequestBody List<Campaign> list) {
        return campaignService.addCampaign(list);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCampaign(@RequestBody List<Campaign> list){return campaignService.updateCampaign(list);}
}
