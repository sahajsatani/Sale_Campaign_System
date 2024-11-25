package com.salecampaign.salescampaign.services;

import com.salecampaign.salescampaign.model.Product;
import com.salecampaign.salescampaign.repositories.DiscountRepo;
import com.salecampaign.salescampaign.repositories.ProductRepo;
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
    @Transactional
    public ResponseEntity<?> registerProduct(List<Product> list) {
        if (list.size() == 0) {
            throw new NoSuchElementException("Not have any list of products to register.");
        }
        try {
            productRepo.saveAll(list);
            return new ResponseEntity<>("Products added.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional
    public ResponseEntity<?> getPageByNumber(int pageNo, int pageSize) {
        try {
            Page<Product> p = productRepo.findAll(PageRequest.of(pageNo,pageSize, Sort.by("productId").ascending()));
            return new ResponseEntity<>(p, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional
    public ResponseEntity<?> deleteProduct(List<Product> list) {
        try {
            if(list.isEmpty())
                throw new NoSuchElementException("Not have any list of product to delete");
            List<String> list1 = list.stream().map(Product::getProductId).toList();
            for (String i : list1){
                discountRepo.deleteAllByProducts(i);
            }
            productRepo.deleteAllById(list1);
            return new ResponseEntity<>("Products are deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @Transactional
//    public ResponseEntity<?> editProduct(List<Product> list) {
//        try {
//            int count = 0;
//            for (Product product : list) {
//                if (productRepo.existsById(product.getProductId())) {
//                    productRepo.save(product);
//                    count++;
//                }
//            }
//            return new ResponseEntity<>(count + " number of product updated.", HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
