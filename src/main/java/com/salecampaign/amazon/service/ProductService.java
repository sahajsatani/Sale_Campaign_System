package com.salecampaign.amazon.service;

import com.salecampaign.amazon.model.Product;
import com.salecampaign.amazon.repositories.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepo productRepo;

    public ResponseEntity<?> addProduct(List<Product> list) {
        try {
            productRepo.saveAll(list);
            return new ResponseEntity<>("Products Added.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getPageByNumber(int pageNo, int pageSize) {
        try {
            Page<Product> p = productRepo.findAll(PageRequest.of(pageNo, pageSize, Sort.by("productId")));
            return new ResponseEntity<>(p, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
