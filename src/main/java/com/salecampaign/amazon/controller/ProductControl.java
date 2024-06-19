package com.salecampaign.amazon.controller;

import com.salecampaign.amazon.model.Product;
import com.salecampaign.amazon.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductControl {
    @Autowired
    ProductService productService;

    @PostMapping("/save")
    public ResponseEntity<?> addProduct(@RequestBody List<Product> list) {
        return productService.addProduct(list);
    }

    @GetMapping("/getPage")
    public ResponseEntity<?> getPageByNumber(
            @RequestParam(value = "pageNo", defaultValue = "3", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return productService.getPageByNumber(pageNo, pageSize);
    }
}
