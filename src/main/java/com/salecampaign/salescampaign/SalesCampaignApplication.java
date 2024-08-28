package com.salecampaign.salescampaign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
@EnableMethodSecurity
public class SalesCampaignApplication {
    public static void main(String[] args) {
        SpringApplication.run(SalesCampaignApplication.class, args);
        System.out.println("API Start...");
    }

}
