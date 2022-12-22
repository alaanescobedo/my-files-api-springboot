package com.alan.apispringboot.auth.services;

import com.alan.apispringboot.security.cookie.JwtCookieProvider;
import com.alan.apispringboot.security.jwt.JwtProvider;
import com.alan.apispringboot.users.entities.User;
import com.alan.apispringboot.users.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.List;

@Service
public class AuthServiceImp implements AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsersService usersService;

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private JwtCookieProvider jwtCookieProvider;

    @Override
    public User getAuthenticatedUser(String username, String plainPassword) {
        User user = usersService.getUserByUsername(username);
        if (!passwordEncoder.matches(plainPassword, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return user;
    }

    @Override
    public ResponseCookie getCookieWithAccessToken(Authentication authentication) {
        String token = jwtProvider.generateAccessToken(authentication);
        return jwtCookieProvider.generateAccessCookie(token);
    }

    @Override
    public ResponseCookie getCookieWithRefreshToken(Authentication authentication) {
        String token = jwtProvider.generateRefreshToken(authentication);
        return jwtCookieProvider.generateRefreshCookie(token);
    }

    @Override
    public List<ResponseCookie> getCookiesForLogout() {
        ResponseCookie atCookie = jwtCookieProvider.clearAccessCookie();
        ResponseCookie rtCookie = jwtCookieProvider.clearRefreshCookie();
        return Arrays.asList(atCookie, rtCookie);
    }


}
