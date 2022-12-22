package com.alan.apispringboot.security.jwt;

import com.alan.apispringboot.security.CurrentUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${jwt.accessSecret}")
    private String atSecret;

    @Value("${jwt.accessExpiration}")
    private int atExpiration;

    @Value("${jwt.refreshSecret}")
    private String rtSecret;

    @Value("${jwt.refreshExpiration}")
    private int rtExpiration;

    public String generateAccessToken(Authentication authentication) {
        logger.info("Generating access token");
        CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
        logger.error(currentUser.getUsername());

        Date expiration = new Date(System.currentTimeMillis() + atExpiration * 1000);
        logger.error(expiration.toString());

        return Jwts.builder()
                .setSubject(currentUser.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, atSecret)
                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        logger.info("Generating refresh token");
        CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
        logger.error(currentUser.getUsername());

        Date expiration = new Date(System.currentTimeMillis() + rtExpiration * 1000);
        logger.error(expiration.toString());

        return Jwts.builder()
                .setSubject(currentUser.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, rtSecret)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        logger.info("Getting username from token");
        return Jwts.parser()
                .setSigningKey(atSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        logger.info("Validating token");
        try {
            Jwts.parserBuilder().setSigningKey(atSecret).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid token");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported token");
        } catch (IllegalArgumentException e) {
            logger.error("Empty token");
        } catch (ExpiredJwtException e) {
            logger.error("Expired token");
        } catch (SignatureException e) {
            logger.error("Invalid signature");
        }
        return false;
    }
}
