package ru.egorov.effectiveexample.util;

import io.jsonwebtoken.Claims;
import ru.egorov.effectiveexample.jwt.JwtAuthentication;

public class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtAuthentication = new JwtAuthentication();
        jwtAuthentication.setUsername(claims.getSubject());
        return jwtAuthentication;
    }
}
