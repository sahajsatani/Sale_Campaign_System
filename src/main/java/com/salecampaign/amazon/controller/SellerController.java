package com.salecampaign.amazon.controller;

import com.salecampaign.amazon.jwt.JwtUtils;
import com.salecampaign.amazon.model.Seller;
import com.salecampaign.amazon.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/seller")
public class SellerController {
    @Autowired
    SellerService sellerService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> loginSeller(@RequestBody Seller seller){
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(seller.getUsername(),seller.getPassword()));
        }
        catch (AuthenticationException e){
            HashMap<String,Object> map = new HashMap<>();
            map.put("message","Bad credential");
            map.put("status",false);
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
        //add authentication in security endpoint(context) holder for authentication
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //just get user detail
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        //generate jwt token
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        //create login response for endpoint res
        HashMap<String,Object> map = new HashMap<>();
        List<String> roles = userDetails
                .getAuthorities()
                .stream()
                .map(i->i.getAuthority())
                .toList();
        map.put("jwtToken",jwtToken);
        map.put("username",userDetails.getUsername());
        map.put("roles",roles);
        return new ResponseEntity<>(map,HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerSeller(@RequestBody Seller seller){
        return sellerService.registerSeller(seller);
    }

    @PostMapping("/addproduct")
    public ResponseEntity<?> addProduct(@RequestBody Seller seller){
        return sellerService.addProduct(seller);
    }
}
