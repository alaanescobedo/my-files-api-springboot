package com.alan.apispringboot.auth.services;

import com.alan.apispringboot.users.entities.User;
import org.springframework.security.core.Authentication;

import javax.servlet.http.Cookie;
import java.util.List;

public interface AuthService {

    public User getAuthenticatedUser(String username, String plainPassword);
    public Cookie getCookieWithAccessToken(Authentication authentication);
    public Cookie getCookieWithRefreshToken(Authentication authentication);
    public List<Cookie> getCookiesForLogout();
}
