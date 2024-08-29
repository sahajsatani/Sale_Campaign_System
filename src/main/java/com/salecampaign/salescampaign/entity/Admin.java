package com.salecampaign.salescampaign.entity;

import com.salecampaign.salescampaign.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tbladmin")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int adminId;

    private String username;

    private String password;

    private String email;

    public Admin setRole(Role role) {
        this.role = role;
        return this;
    }

    private Role role = Role.USER;

    //Mapping
    @OneToMany(mappedBy = "admin",cascade = CascadeType.ALL)
    private List<Product> products;

    private int totalProducts;
}
