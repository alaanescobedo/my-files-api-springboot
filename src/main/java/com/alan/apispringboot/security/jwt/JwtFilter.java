package com.alan.apispringboot.security.jwt;

import com.alan.apispringboot.security.services.UserDetailsServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;
    @Value("${jwt.accessTokenCookieName}")
    private String atCookieName;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("doFilterInternal");
        try {
            String token = getToken(request);
            logger.info("token: " + token);
            if (token != null && jwtProvider.validateToken(token)) {
                logger.info("token is valid");
                String userName = jwtProvider.getUsernameFromToken(token);
                logger.info("userName: " + userName);
                UserDetails userDetails = userDetailsServiceImp.loadUserByUsername(userName);
                logger.info("userDetails: " + userDetails);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                logger.info("auth: " + auth);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            logger.error("Fail in set user authentication ", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, atCookieName);
        logger.info("cookie: " + cookie.getValue());
        return cookie != null ? cookie.getValue() : null;
    }
}
