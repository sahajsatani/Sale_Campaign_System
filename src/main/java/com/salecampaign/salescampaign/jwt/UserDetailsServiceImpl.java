package com.salecampaign.salescampaign.jwt;

import com.salecampaign.salescampaign.entity.enums.Role;
import com.salecampaign.salescampaign.entity.Admin;
import com.salecampaign.salescampaign.repositories.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    AdminRepo adminRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            List<Object[]> objects = adminRepo.findByUsername(username);
            if (!objects.isEmpty()) {
                Admin admin = new Admin();
                admin.setAdminId((int) objects.getFirst()[0]);
                admin.setUsername((String) objects.getFirst()[1]);
                admin.setPassword((String) objects.getFirst()[2]);
                if (objects.getFirst()[3] == "0") {
                    admin.setRole(Role.USER);
                } else if (objects.getFirst()[3] == "1") {
                    admin.setRole(Role.ADMIN);
                }
                return new UserDetailsImpl(admin);
            } else {
                throw new UsernameNotFoundException(username + " not found.");
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException(username + " not found.");
        }
    }
}
