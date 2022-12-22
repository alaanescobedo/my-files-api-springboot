package com.alan.apispringboot.auth.services;

import com.alan.apispringboot.users.entities.User;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;

import javax.servlet.http.Cookie;
import java.util.List;

public interface AuthService {

    public User getAuthenticatedUser(String username, String plainPassword);
    public ResponseCookie getCookieWithAccessToken(Authentication authentication);
    public ResponseCookie getCookieWithRefreshToken(Authentication authentication);
    public List<ResponseCookie> getCookiesForLogout();
}
