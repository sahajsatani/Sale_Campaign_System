package com.salecampaign.amazon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
public class AmazonApplication {
    public static void main(String[] args) {
        SpringApplication.run(AmazonApplication.class, args);
        System.out.println("API Start...");
    }

}
