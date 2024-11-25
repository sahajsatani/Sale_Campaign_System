package com.salecampaign.salescampaign.scheduler;

import com.salecampaign.salescampaign.model.Campaign;
import com.salecampaign.salescampaign.model.Discount;
import com.salecampaign.salescampaign.model.enums.CampaignStatus;
import com.salecampaign.salescampaign.model.History;
import com.salecampaign.salescampaign.model.Product;
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
    private final Queue<ProductRetryDTO> retryQueue = new ConcurrentLinkedQueue<>();
    @Scheduled(cron = "0 34 19 * * *")
    @Async
    @Transactional
    public void campaignStart() {
        List<Object[]> campList = campaignRepo.getCampaignStartByCid();
        if (campList.isEmpty()) return;
        campList.forEach(this::processStart);
        processStartRetryQueue();
    }

    @Scheduled(cron = "0 43 19 * * *")
    @Async
    @Transactional
    public void campaignStop() {
        List<Object[]> campList = campaignRepo.getCampaignStopByCid();
        if (campList.isEmpty()) return;
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
            List<Discount> discountList = discountRepo.findAllByCampaign((int) campI[0]);
            discountList.forEach(i -> {
                try {
                    Product product = productRepo.findById(i.getProduct().getProductId()).get();
                    if (product == null) throw new NullPointerException();

                    int discount = i.getDiscount();
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
                catch (NullPointerException _) {}
                catch (Exception e) {
                    retryQueue.add(new ProductRetryDTO(i.getProduct().getProductId(), i.getDiscount()));
                }
            });

            //for add discount and update current price
            productRepo.saveAll(products);
            //for store history of updated discount and price
            historyRepo.saveAll(histories);
            campaign.setStatus(CampaignStatus.CURRENT);
            campaignRepo.save(campaign); // for update campaign status
            System.out.println(new Date());
            long end = System.currentTimeMillis();
            System.out.println(campaign.getTitle() + " Compaign Start.. in " + (end - start));
        }
    }
    //this method stop the campaign
    private void processStop(Object[] camI) {
        long start = System.currentTimeMillis();
        System.out.println(new Date());
        List<Product> products = new ArrayList<>();
        List<History> histories = new ArrayList<>();

        Campaign campaign = campaignRepo.findById((int) camI[0]).get();
        List<Discount> discountList = discountRepo.findAllByCampaign((int) camI[0]);

        discountList.forEach(i -> {
            try {
                Product product = productRepo.findById(i.getProduct().getProductId()).get();
                if (product == null) throw new NullPointerException();

                int discount = i.getDiscount();
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
            } catch (NullPointerException _) {
            } catch (Exception e) {
                retryQueue.add(new ProductRetryDTO(i.getProduct().getProductId(), i.getDiscount()));
            }
        });

        //for add discount and update current price
        productRepo.saveAll(products);
        //for store history of updated discount and price
        historyRepo.saveAll(histories);
        System.out.println(new Date());
        campaign.setStatus(CampaignStatus.PAST);
        campaignRepo.save(campaign); // for update campaign status
        long end = System.currentTimeMillis();
        System.out.println(campaign.getTitle() + " Compaign Stop.. in " + (end - start));
    }
    //this method retry to store that changes those not performed in process start method
    private void processStartRetryQueue() {
        int attempt = 0;
        while (attempt < 3 && !retryQueue.isEmpty()) {
            List<Product> products = new ArrayList<>();
            List<History> histories = new ArrayList<>();
            for (int i = 0; i < retryQueue.size(); i++) {
                ProductRetryDTO productRetryDTO = retryQueue.poll();
                try {
                    Product product = productRepo.findById(productRetryDTO.getProductId()).get();
                    if (product == null) throw new NullPointerException();

                    int discount = productRetryDTO.getDiscount();
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

                } catch (NullPointerException _) {
                } catch (Exception e) {
                    retryQueue.add(new ProductRetryDTO(productRetryDTO.getProductId(), productRetryDTO.getDiscount()));
                }
            }
            productRepo.saveAll(products);
            historyRepo.saveAll(histories);
            attempt++;
            System.out.println("Attempt " + attempt);
        }
    }
    //this method retry to store that changes those not performed in process start method
    private void processStopRetryQueue() {
        int attempt = 0;
        while (attempt < 3 && !retryQueue.isEmpty()) {
            List<Product> products = new ArrayList<>();
            List<History> histories = new ArrayList<>();

            for (int i = 0; i < retryQueue.size(); i++) {
                ProductRetryDTO productRetryDTO = retryQueue.poll();
                try {
                    Product product = productRepo.findById(productRetryDTO.getProductId()).get();
                    if (product == null) throw new NullPointerException();

                    int discount = productRetryDTO.getDiscount();
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
                } catch (NullPointerException _) {
                } catch (Exception e) {
                    retryQueue.add(new ProductRetryDTO(productRetryDTO.getProductId(), productRetryDTO.getDiscount()));
                }


                productRepo.saveAll(products);
                historyRepo.saveAll(histories);
            }
            attempt++;
            System.out.println("Attempt " + attempt);
        }
    }
}

