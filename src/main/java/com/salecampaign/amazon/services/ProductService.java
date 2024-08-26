package com.salecampaign.amazon.services;

import com.salecampaign.amazon.entity.Product;
import com.salecampaign.amazon.repositories.DiscountRepo;
import com.salecampaign.amazon.repositories.HistoryRepo;
import com.salecampaign.amazon.repositories.ProductRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {
    @Autowired
    ProductRepo productRepo;
    @Autowired
    DiscountRepo discountRepo;

    @Autowired
    HistoryRepo historyRepo;

    @Transactional
    public ResponseEntity<?> addProduct(List<Product> list) {
        if (list.size() == 0) {
            throw new NoSuchElementException("Not have any list of products.");
        }
        try {
            productRepo.saveAll(list);
            return new ResponseEntity<>("Products Added.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> getPageByNumber(int pageNo, int pageSize) {
        try {
            Page<Product> p = productRepo.findAll(PageRequest.of(pageNo, pageSize, Sort.by("productId")));
            return new ResponseEntity<>(p, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> updateProduct(List<Product> list) {
        try {
            int count = 0;
            for (Product product : list) {
                if (productRepo.existsById(product.getProductId())) {
                    productRepo.save(product);
                    count++;
                }
            }
            return new ResponseEntity<>(count + " number of product updated.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteProduct(List<Product> list) {
        try {
            List<String> list1 = list.stream().map(Product::getProductId).toList();
            for (String i : list1){
                discountRepo.deleteAllByProduct(i);
            }
            productRepo.deleteAllById(list1);
            return new ResponseEntity<>("Products are deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
