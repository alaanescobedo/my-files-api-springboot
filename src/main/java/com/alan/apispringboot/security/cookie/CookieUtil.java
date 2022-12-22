package com.alan.apispringboot.security.cookie;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

@Component
public class CookieUtil {

    @Value("${cookie.domain}")
    private String domain;
    @Value("${cookie.path}")
    private String path;
    @Value("${cookie.secure}")
    private boolean secure;
    @Value("${cookie.httpOnly}")
    private boolean httpOnly;


    public ResponseCookie create(String name, String value, int maxAge) {
        return ResponseCookie.from(name, value)
                .maxAge(maxAge)
                .domain(domain)
                .path(path)
                .secure(secure)
                .httpOnly(httpOnly)
                .build();
    }

    public ResponseCookie clear(String name) {
        return create(name, null,1);
    }
}
