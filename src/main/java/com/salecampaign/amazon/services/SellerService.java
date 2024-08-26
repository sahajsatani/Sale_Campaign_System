package com.salecampaign.amazon.services;

import com.salecampaign.amazon.entity.Product;
import com.salecampaign.amazon.entity.Seller;
import com.salecampaign.amazon.repositories.SellerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class SellerService {
    @Autowired
    SellerRepo sellerRepo;

    public ResponseEntity<?> registerSeller(Seller seller) {
        HashMap<String,String> map = new HashMap<>();
        try {
            List<Product> products = seller.getProducts();
            for (Product i : products)
                i.setSeller(seller);
            seller.setProducts(products);
            if (sellerRepo.save(seller)!=null){
                map.put("status","Process completed");
                map.put("message","New seller added.");
                return new ResponseEntity<>(map,HttpStatus.OK);
            }
            else{
                map.put("status","Process completed");
                map.put("message","Not seller added.");
                return new ResponseEntity<>(map,HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(map,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> addProduct(Seller seller) {
        HashMap<String,String> map = new HashMap<>();
        try {
            List<Product> products = seller.getProducts();
            for (Product i : products)
                i.setSeller(seller);
            seller.setProducts(products);
            if (sellerRepo.save(seller)!=null){
                map.put("status","Process completed");
                map.put("message","Products added.");
                return new ResponseEntity<>(map,HttpStatus.OK);
            }
            else{
                map.put("status","Process completed");
                map.put("message","Not any one product added.");
                return new ResponseEntity<>(map,HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(map,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
