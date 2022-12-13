package com.alan.apispringboot.security.cookie;

import com.alan.apispringboot.security.jwt.JwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import java.util.Date;

@Component
public class JwtCookieProvider {
    private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class);

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



    @Autowired
    private CookieUtil cookieUtil;


    public Cookie generateAccessCookie(String token) {
        return cookieUtil.create(atCookieName, token, false, -1, domain);
    }

    public Cookie generateRefreshCookie(String token) {
        return cookieUtil.create(rtCookieName, token, false, -1, domain);
    }

    public Cookie clearAccessCookie() {
        return cookieUtil.clear(atCookieName, domain);
    }

    public Cookie clearRefreshCookie() {
        return cookieUtil.clear(rtCookieName, domain);
    }

}
