package com.alan.apispringboot.security.cookie;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Component
public class CookieUtil {

    public static Cookie create(String name, String value, boolean secure, int maxAge, String domain) {
        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(secure);
        cookie.setHttpOnly(false);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setDomain(domain);
        return cookie;
    }

    public static Cookie clear(String name, String domain) {
        return create(name, null, true, 1, domain);
    }
}
