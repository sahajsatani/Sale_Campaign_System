package com.salecampaign.salescampaign.controller;

import com.salecampaign.salescampaign.entity.Product;
import com.salecampaign.salescampaign.services.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    //Security config practice
//    @GetMapping("/getCsrf")
//    public CsrfToken add(HttpServletRequest request){
//        return (CsrfToken) request.getAttribute("_csrf");
//    }
//
//    @PreAuthorize("hasRole('USER')")
//    @GetMapping("/ok")
//    public String getOk(HttpServletRequest request){
//        return "Hey Sahaj "+request.getSession().getId();
//    }

    @PostMapping("/register")
    public ResponseEntity<?> registerProduct(@RequestBody List<Product> list) {
        return productService.registerProduct(list);
    }

//    @PutMapping("/edit")
//    public ResponseEntity<?> editProduct(@RequestBody List<Product> list){return productService.editProduct(list);}

    @GetMapping("/getPage")
    public ResponseEntity<?> getPageByNumber(
            @RequestParam(value = "pageNo", defaultValue = "3", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return productService.getPageByNumber(pageNo, pageSize);
    }

    @DeleteMapping("/remove")
    public  ResponseEntity<?> deleteProduct(@RequestBody List<Product> list){return productService.deleteProduct(list);}
}

