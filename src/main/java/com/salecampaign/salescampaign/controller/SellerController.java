package com.salecampaign.salescampaign.controller;


import com.salecampaign.salescampaign.entity.Seller;
import com.salecampaign.salescampaign.services.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/seller")
public class SellerController {
    @Autowired
    SellerService sellerService;


    @GetMapping("/getname")
    public String getName(){
        System.out.println("ok");
        return "Sahaj";
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginSeller(@RequestBody Seller seller) {return sellerService.loginService(seller);}
    @PostMapping("/register")
    public ResponseEntity<?> registerSeller(@RequestBody Seller seller){
        return sellerService.registerSeller(seller);
    }

    @PostMapping("/addproduct")
    public ResponseEntity<?> addProduct(@RequestBody Seller seller){
        return sellerService.addProduct(seller);
    }
}
