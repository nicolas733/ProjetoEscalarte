package br.com.sistemacadastro.sistemacadastro.modules.collaborator.services;
/*
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.boot.web.server.Ssl.ClientAuth.map;


public class CookieService {
    public static void setCookie(HttpServletResponse response, String key, String value, int seconds) throws UnsupportedEncodingException {
        Cookie cookie = new Cookie(key, URLEncoder.encode(value, "UTF-8"));
        cookie.setMaxAge(seconds);
        response.addCookie(cookie);
    }
    public static String getCookie(HttpServletRequest request, String key) {
        String value = Optional.ofNullable(request.getCookies())
                .flatMap(cookies -> Arrays.stream(cookies)
                .filter(cookie -> key.equals(cookie.getName)))
                .findAny()
                ).map(e -> e.getValue())
                .orElse(null);
    }

}

 */

