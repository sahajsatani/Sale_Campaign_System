package com.salecampaign.amazon.sheduler;

import com.salecampaign.amazon.model.Campaign;
import com.salecampaign.amazon.model.History;
import com.salecampaign.amazon.model.Product;
import com.salecampaign.amazon.repositories.CampaignRepos;
import com.salecampaign.amazon.repositories.DiscountRepo;
import com.salecampaign.amazon.repositories.HistoryRepo;
import com.salecampaign.amazon.repositories.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Configuration
@EnableScheduling
public class UserScheduler {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    HistoryRepo historyRepo;

    @Autowired
    DiscountRepo discountRepo;

    @Autowired
    CampaignRepos campaignRepo;

    @Scheduled(cron = "0 0 0 * * *")
    public void campaignStart() {
        List<Object[]> campList = campaignRepo.getCampaignStartByCid();
        if (campList.isEmpty())
            return;

        System.out.println("Compaign Start..");
        for (Object[] campI : campList) {

            Campaign campaign = campaignRepo.findById((int) campI[0]).get();
            List<Object[]> discountList = discountRepo.getDiscountsByCampId((int) campI[0]);

            for (Object[] i : discountList) {
                Product product = productRepo.findById((String) i[1]).get();
                int discount = (int) i[0];

                int oldCur = product.getCurrentPrice();
                int newPrice = ((product.getMrp() * discount) / 100);
                product.setCurrentPrice((oldCur - newPrice));
                product.setDiscount(product.getDiscount() + discount);
                productRepo.save(product);
                System.out.println("product Saved");

                History history = new History();
                history.setDate(LocalDate.now());
                history.setBeforeDiscountPrice(oldCur);
                history.setAfterDiscountPrice(product.getCurrentPrice());
                history.setProduct(product);
                historyRepo.save(history);
                System.out.println("history Saved");
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void campaignStop() {
        List<Object[]> campList = campaignRepo.getCampaignStopByCid();
        if (campList.isEmpty())
            return;

        System.out.println("Compaign Stop..");
        for (Object[] camI : campList) {
            Campaign campaign = campaignRepo.findById((int) camI[0]).get();
            List<Object[]> discountList = discountRepo.getDiscountsByCampId((int) camI[0]);

            for (Object[] i : discountList) {
                Product product = productRepo.findById((String) i[1]).get();
                int discount = (int) i[0];

                int oldCur = product.getCurrentPrice();
                int newPrice = ((product.getMrp() * discount) / 100);
                product.setCurrentPrice((oldCur + newPrice));
                product.setDiscount(product.getDiscount() - discount);
                productRepo.save(product);
                System.out.println("product Saved");

                History history = new History();
                history.setDate(LocalDate.now());
                history.setBeforeDiscountPrice(oldCur);
                history.setAfterDiscountPrice(product.getCurrentPrice());
                history.setProduct(product);
                historyRepo.save(history);
                System.out.println("history Saved");

            }
        }
    }
}
