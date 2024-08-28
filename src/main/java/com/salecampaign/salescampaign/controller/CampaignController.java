package com.salecampaign.salescampaign.controller;

import com.salecampaign.salescampaign.entity.Campaign;
import com.salecampaign.salescampaign.services.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campaign")
public class CampaignController {

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
