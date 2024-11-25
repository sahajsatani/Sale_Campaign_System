package com.salecampaign.salescampaign.model;

import com.salecampaign.salescampaign.model.enums.CampaignStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tblcampaign")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int campaignId;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private CampaignStatus Status = CampaignStatus.UPCOMING;

    //Mapping
    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Discount> discounts = new ArrayList<>();
    public Campaign setStartDate(String startDate) {
        this.startDate = LocalDate.parse(startDate);
        return this;
    }
    public Campaign setEndDate(String endDate) {
        this.endDate = LocalDate.parse(endDate).plusDays(1);
        return this;
    }
    public Campaign setNormalEndDate(String endDate) {
        this.endDate = LocalDate.parse(endDate);
        return this;
    }
}
