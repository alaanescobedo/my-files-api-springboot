package com.alan.apispringboot.security.cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

@Component
public class JwtCookieProvider {

    @Value("${jwt.accessTokenCookieName}")
    private String atCookieName;
    @Value("${jwt.accessExpiration}")
    private int atExpiration;
    @Value("${jwt.refreshTokenCookieName}")
    private String rtCookieName;
    @Value("${jwt.refreshExpiration}")
    private int rtExpiration;
    @Value("${cookie.domain}")
    private String domain;

    @Autowired
    private CookieUtil cookieUtil;

    public ResponseCookie generateAccessCookie(String token) {
        return cookieUtil.create(atCookieName, token, atExpiration);
    }

    public ResponseCookie generateRefreshCookie(String token) {
        return cookieUtil.create(rtCookieName, token, rtExpiration);
    }

    public ResponseCookie clearAccessCookie() {
        return cookieUtil.clear(atCookieName);
    }

    public ResponseCookie clearRefreshCookie() {
        return cookieUtil.clear(rtCookieName);
    }

}
