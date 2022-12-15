package com.alan.apispringboot.security.cookie;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

@Component
public class JwtCookieProvider {

    @Value("${jwt.accessTokenCookieName}")
    private String atCookieName;
    @Value("${jwt.AccessExpiration}")
    private int atExpiration;
    @Value("${jwt.refreshTokenCookieName}")
    private String rtCookieName;
    @Value("${jwt.refreshExpiration}")
    private int rtExpiration;
    @Value("${cookie.domain}")
    private String domain;

    public Cookie generateAccessCookie(String token) {
        return CookieUtil.create(atCookieName, token, false, -1, domain);
    }

    public Cookie generateRefreshCookie(String token) {
        return CookieUtil.create(rtCookieName, token, false, -1, domain);
    }

    public Cookie clearAccessCookie() {
        return CookieUtil.clear(atCookieName, domain);
    }

    public Cookie clearRefreshCookie() {
        return CookieUtil.clear(rtCookieName, domain);
    }

}
