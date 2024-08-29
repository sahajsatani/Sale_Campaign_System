package com.salecampaign.salescampaign.services;

import com.salecampaign.salescampaign.entity.Product;
import com.salecampaign.salescampaign.entity.Admin;
import com.salecampaign.salescampaign.jwt.JwtService;
import com.salecampaign.salescampaign.repositories.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    AdminRepo adminRepo;

    @Autowired
    AuthenticationManager authenticationManager;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    @Autowired
    JwtService jwtService;

    public ResponseEntity<?> registerAdmin(Admin admin) {
        HashMap<String, String> map = new HashMap<>();
        try {
            admin.setPassword(bCryptPasswordEncoder.encode(admin.getPassword()));

            if (admin.getProducts() != null) {
                List<Product> products = admin.getProducts();
                for (Product i : products)
                    i.setAdmin(admin);
                admin.setProducts(products);
            }

            if (adminRepo.save(admin) != null) {
                map.put("status", "Process completed");
                map.put("message", "New admin register.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            } else {
                map.put("status", "Process completed");
                map.put("message", "Not admin register.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

        } catch (Exception e) {
            map.put("message", e.getMessage());
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> registerProduct(Admin admin) {
        HashMap<String, String> map = new HashMap<>();
        try {
            List<Product> products = admin.getProducts();
            for (Product i : products)
                i.setAdmin(admin);
            admin.setProducts(products);
            if (adminRepo.save(admin) != null) {
                map.put("status", "Process completed");
                map.put("message", "Products register.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            } else {
                map.put("status", "Process completed");
                map.put("message", "Not any one product register.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> loginAdmin(Admin admin) {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(admin.getUsername(), admin.getPassword()));
            if (authentication.isAuthenticated()) {
                return new ResponseEntity<>(jwtService.generateToken(admin.getUsername()), HttpStatus.OK);
            }
            return new ResponseEntity<>("Not authorized.", HttpStatus.UNAUTHORIZED);

        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
