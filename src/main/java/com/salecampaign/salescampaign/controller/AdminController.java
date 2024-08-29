package com.salecampaign.salescampaign.controller;


import com.salecampaign.salescampaign.entity.Admin;
import com.salecampaign.salescampaign.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody Admin admin) {
        return adminService.loginAdmin(admin);
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@RequestBody Admin admin){
        return adminService.registerAdmin(admin);
    }

    @PostMapping("/registerProduct")
    public ResponseEntity<?> addProduct(@RequestBody Admin admin){
        return adminService.registerProduct(admin);
    }
}
