package com.salecampaign.salescampaign.scheduler;

import com.salecampaign.salescampaign.entity.Campaign;
import com.salecampaign.salescampaign.entity.enums.CampaignStatus;
import com.salecampaign.salescampaign.entity.History;
import com.salecampaign.salescampaign.entity.Product;
import com.salecampaign.salescampaign.repositories.CampaignRepo;
import com.salecampaign.salescampaign.repositories.DiscountRepo;
import com.salecampaign.salescampaign.repositories.HistoryRepo;
import com.salecampaign.salescampaign.repositories.ProductRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

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
    CampaignRepo campaignRepo;

    private final Queue<ProductRetryInfo> retryQueue = new ConcurrentLinkedQueue<>();

    @Scheduled(cron = "0 0 0 * * *")
    @Async
    @Transactional
    public void campaignStart() {
        List<Object[]> campList = campaignRepo.getCampaignStartByCid();
        if (campList.isEmpty())
            return;
        campList.forEach(this::processStart);
        processStartRetryQueue();
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Async
    @Transactional
    public void campaignStop() {
        List<Object[]> campList = campaignRepo.getCampaignStopByCid();
        if (campList.isEmpty())
            return;
        campList.forEach(this::processStop);
        processStopRetryQueue();
    }

    //this method start the campaign
    private void processStart(Object[] campI) {
        {
            long start = System.currentTimeMillis();
            System.out.println(new Date());
            List<Product> products = new ArrayList<>();
            List<History> histories = new ArrayList<>();

            Campaign campaign = campaignRepo.findById((int) campI[0]).get();
            List<Object[]> discountList = discountRepo.getDiscountsByCampId((int) campI[0]);

            discountList.forEach(i -> {

                try{
                    Product product = productRepo.findById((String) i[1]).get();
                    if (product == null)
                        throw new NullPointerException();

                    int discount = (int) i[0];
                    int oldCur = product.getCurrentPrice();
                    product.setCurrentPrice((oldCur - ((product.getMrp() * discount) / 100)));
                    product.setDiscount(product.getDiscount() + discount);
                    products.add(product);

                    History history = new History();
                    history.setDate(LocalDate.now());
                    history.setBeforeDiscountPrice(oldCur);
                    history.setAfterDiscountPrice(product.getCurrentPrice());
                    history.setProduct(product);
                    histories.add(history);
                }
                catch (NullPointerException _){}
                catch (Exception e){
                    retryQueue.add(new ProductRetryInfo((String) i[1],(int) i[0]));
                }
            });

            //for add discount and update current price
            productRepo.saveAll(products);
            System.out.println("product Saved");

            //for store history of updated discount and price
            historyRepo.saveAll(histories);
            System.out.println("history Saved");

            campaign.setStatus(CampaignStatus.CURRENT);
            campaignRepo.save(campaign); // for update campaign status
            System.out.println(new Date());
            long end = System.currentTimeMillis();
            System.out.println(campI[1] + "Compaign Start.. in " + (end - start));
        }
    }
    //this method retry to store that changes those not performed in process start method
    private void processStartRetryQueue() {
        int attempt=0;
        while (attempt<3 && !retryQueue.isEmpty()){
            List<Product> products = new ArrayList<>();
            List<History> histories = new ArrayList<>();
            for(int i=0; i< retryQueue.size(); i++) {
                ProductRetryInfo productRetryInfo = retryQueue.poll();
                try{
                    Product product = productRepo.findById(productRetryInfo.getProductId()).get();
                    if (product == null)
                        throw new NullPointerException();

                    int discount = productRetryInfo.getDiscount();
                    int oldCur = product.getCurrentPrice();
                    product.setCurrentPrice((oldCur - ((product.getMrp() * discount) / 100)));
                    product.setDiscount(product.getDiscount() + discount);
                    products.add(product);

                    History history = new History();
                    history.setDate(LocalDate.now());
                    history.setBeforeDiscountPrice(oldCur);
                    history.setAfterDiscountPrice(product.getCurrentPrice());
                    history.setProduct(product);
                    histories.add(history);

                }
                catch (NullPointerException _){}
                catch (Exception e){
                    retryQueue.add(new ProductRetryInfo(productRetryInfo.getProductId(),productRetryInfo.getDiscount()));
                }
            }
            productRepo.saveAll(products);
            historyRepo.saveAll(histories);
            attempt++;
            System.out.println("Attempt " + attempt);
        }
    }

    //this method stop the campaign
    private void processStop(Object[] camI){
        long start = System.currentTimeMillis();
        System.out.println(new Date());
        List<Product> products = new ArrayList<>();
        List<History> histories = new ArrayList<>();

        Campaign campaign = campaignRepo.findById((int) camI[0]).get();
        List<Object[]> discountList = discountRepo.getDiscountsByCampId((int) camI[0]);

        discountList.forEach (i -> {
            try{
                Product product = productRepo.findById((String) i[1]).get();
                if (product == null)
                    throw new NullPointerException();

                int discount = (int) i[0];
                int oldCur = product.getCurrentPrice();
                product.setCurrentPrice((oldCur + ((product.getMrp() * discount) / 100)));
                product.setDiscount(product.getDiscount() - discount);
                products.add(product);

                History history = new History();
                history.setDate(LocalDate.now());
                history.setBeforeDiscountPrice(oldCur);
                history.setAfterDiscountPrice(product.getCurrentPrice());
                history.setProduct(product);
                histories.add(history);
            }
            catch (NullPointerException _){}
            catch (Exception e){
                retryQueue.add(new ProductRetryInfo((String) i[1],(int) i[0]));
            }
        });
        //for add discount and update current price
        productRepo.saveAll(products);
        System.out.println("product Saved");

        //for store history of updated discount and price
        historyRepo.saveAll(histories);
        System.out.println("history Saved");

        System.out.println(new Date());

        campaign.setStatus(CampaignStatus.PAST);
        campaignRepo.save(campaign); // for update campaign status

        long end = System.currentTimeMillis();
        System.out.println(camI[1] + "Compaign Stop.. in "+(end-start));
    }
    //this method retry to store that changes those not performed in process start method
    private void processStopRetryQueue() {
        int attempt=0;
        while (attempt<3 && !retryQueue.isEmpty()){
            List<Product> products = new ArrayList<>();
            List<History> histories = new ArrayList<>();

            for(int i=0; i< retryQueue.size(); i++) {
                ProductRetryInfo productRetryInfo = retryQueue.poll();
                try{
                    Product product = productRepo.findById(productRetryInfo.getProductId()).get();
                    if (product == null)
                        throw new NullPointerException();

                    int discount = productRetryInfo.getDiscount();
                    int oldCur = product.getCurrentPrice();
                    product.setCurrentPrice((oldCur + ((product.getMrp() * discount) / 100)));
                    product.setDiscount(product.getDiscount() - discount);
                    products.add(product);

                    History history = new History();
                    history.setDate(LocalDate.now());
                    history.setBeforeDiscountPrice(oldCur);
                    history.setAfterDiscountPrice(product.getCurrentPrice());
                    history.setProduct(product);
                    histories.add(history);
                }
                catch (NullPointerException _){}
                catch (Exception e){
                    retryQueue.add(new ProductRetryInfo(productRetryInfo.getProductId(),productRetryInfo.getDiscount()));
                }


                productRepo.saveAll(products);
                historyRepo.saveAll(histories);
            }
            attempt++;
            System.out.println("Attempt " + attempt);
        }
    }
}

