package com.salecampaign.salescampaign.jwt;

import com.salecampaign.salescampaign.entity.Role;
import com.salecampaign.salescampaign.entity.Seller;
import com.salecampaign.salescampaign.repositories.SellerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    SellerRepo sellerRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            List<Object[]> objects = sellerRepo.findByUsername(username);
            if(!objects.isEmpty()){
                Seller seller = new Seller();
                seller.setSellerId((int) objects.get(0)[0]);
                seller.setUsername((String) objects.get(0)[1]);
                seller.setPassword((String) objects.get(0)[2]);
                seller.setRole(Role.USER);
                return new UserDetailsImpl(seller);
            }else{
                throw new UsernameNotFoundException(username+" not found.");
            }
        }catch (Exception e){
            throw new UsernameNotFoundException(username+" not found.");
        }
    }
}
