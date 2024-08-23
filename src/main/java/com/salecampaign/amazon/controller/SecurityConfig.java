package com.salecampaign.amazon.controller;

import com.salecampaign.amazon.jwt.AuthEntryPointJwt;
import com.salecampaign.amazon.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
    DataSource dataSource;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;
    @Bean
    public AuthTokenFilter authTokenFilter(){
        return new AuthTokenFilter();
    }

    @Autowired
    UserDetails userDetails;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(withDefaults())//disable cross site request forgery
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/seller/login").permitAll()
                        .anyRequest().authenticated())
//                .formLogin(withDefaults())
//                .httpBasic(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//make session stateless for change it in every call of endpoint
                .exceptionHandling(e -> e.authenticationEntryPoint(unauthorizedHandler))//add our own exception handling when unauthorized user will get
                .addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);//authTokenFilter will check first then check UsernamePasswordAuthenticationFilter
        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails user1 = User.withUsername("pal")
//                .password(bCryptPasswordEncoder().encode("pal@123"))
//                .roles("USER")
//                .build();
//        UserDetails admin = User.withUsername("dhrul")
//                .password(bCryptPasswordEncoder().encode("dhrul@123"))
//                .roles("ADMIN")
//                .build();
//
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//        jdbcUserDetailsManager.createUser(user1);
//        jdbcUserDetailsManager.createUser(admin);
//        return jdbcUserDetailsManager;
////        return new InMemoryUserDetailsManager(user1,admin);
//    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public AuthenticationProvider authenticationProvider(){
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        return provider;
//    }
}
