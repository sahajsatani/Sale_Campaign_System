package com.salecampaign.amazon.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jetSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpirationMils}")
    private String jwtExpirationMils;

    //It's used for extract JWT token from header when user make request
    public String getJwtFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        logger.debug("Authorization Header: {}",bearerToken);
        if (bearerToken!=null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    //It's used for create JWT from Username & Secret key
    public String generateTokenFromUsername(UserDetails userDetails){
        String username = userDetails.getUsername();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+jwtExpirationMils))
                .signWith(key())
                .compact();
    }

    //That make keys from jwt secret key
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    //Extract username from jwt token
    public String getUsernameFromJwtToken(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    //authenticate jwt token by secret key
    public boolean validateJwtToken(String authToken){
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        }catch (MalformedJwtException e){
            logger.error("Invalid JWT token: {}",e.getMessage());
        }catch (ExpiredJwtException e){
            logger.error("JWT token is expired: {}",e.getMessage());
        }catch (UnsupportedJwtException e){
            logger.error("JWT token is unsupported: {}",e.getMessage());
        }catch (IllegalArgumentException e){
            logger.error("JWT claims string is empty: {}",e.getMessage());
        }
        return false;
    }
}
