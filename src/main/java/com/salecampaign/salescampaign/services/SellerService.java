package com.salecampaign.salescampaign.services;

import com.salecampaign.salescampaign.entity.Product;
import com.salecampaign.salescampaign.entity.Seller;
import com.salecampaign.salescampaign.jwt.JwtService;
import com.salecampaign.salescampaign.repositories.SellerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;

@Service
public class SellerService {
    @Autowired
    SellerRepo sellerRepo;

    @Autowired
    AuthenticationManager authenticationManager;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    @Autowired
    JwtService jwtService;

    public ResponseEntity<?> registerSeller(Seller seller) {
            HashMap<String,String> map = new HashMap<>();
        try {
            seller.setPassword(bCryptPasswordEncoder.encode(seller.getPassword()));

            if(seller.getProducts()!=null){
                List<Product> products = seller.getProducts();
                for (Product i : products)
                    i.setSeller(seller);
                seller.setProducts(products);
            }

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
            map.put("message",e.getMessage());
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

    public ResponseEntity<?> loginService(Seller seller) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(seller.getUsername(),seller.getPassword()));
        if (authentication.isAuthenticated()){
            return new ResponseEntity<>(jwtService.generateToken(seller.getUsername()),HttpStatus.OK);
        }
        return new ResponseEntity<>("Not authenticated.",HttpStatus.OK);
    }
}
