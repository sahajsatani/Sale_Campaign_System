package com.salecampaign.amazon.service;

import com.salecampaign.amazon.model.Product;
import com.salecampaign.amazon.repositories.ProductRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductServiceTests {

    @Autowired
    ProductRepo productRepo;

    @Test
    public void testProductService(){
        Product product = new Product();
        product.setProductId("PDVNL3R4D");
        product.setTitle("Gas");
        product.setDescription("Fire Gas");
        product.setMrp(999);
        product.setCurrentPrice(999);
        product.setDiscount(0);
        product.setInventoryCount(100);

        productRepo.save(product);

//        assertEquals(2,1+1);
        assertNotNull(productRepo.findById("PDVNL3R4D"));
    }


//    @ParameterizedTest
//    @ValueSource(strings = {
//            "P1A2B3C5",
//            "P1A2B3Kp"
//    })
//    public void UserTestCase(String st){
//        assertNotNull(productRepo.findById(st));
//    }

}
