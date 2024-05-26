package ru.egorov.effectiveexample.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.egorov.effectiveexample.model.User;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Component
@Slf4j
public class JwtProvider {

    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;
    private final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
    @Value("${jwt.lifetime.access.minutes}")
    private long LIFE_ACCESS_TOKEN;
    @Value("${jwt.lifetime.refresh.day}")
    private long LIFE_REFRESH_TOKEN;

    public JwtProvider(@Value("${jwt.secret.access}") String jwtAccessSecret,
                       @Value("${jwt.secret.refresh}") String jwtRefreshSecret) {
        this.jwtAccessSecret = new SecretKeySpec(jwtAccessSecret.getBytes(StandardCharsets.UTF_8), HS256.getJcaName());
        this.jwtRefreshSecret = new SecretKeySpec(jwtRefreshSecret.getBytes(StandardCharsets.UTF_8), HS256.getJcaName());
    }

    public String generatedAccessToken(@Nonnull User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(LIFE_ACCESS_TOKEN).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);

        return Jwts.builder()
                .setSubject(user.getLogin())
                .setExpiration(accessExpiration)
                .signWith(jwtAccessSecret)
                .claim("user", user.getFirstName())
                .compact();
    }

    public String generatedRefreshToken(@Nonnull User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(LIFE_REFRESH_TOKEN).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);

        return Jwts.builder()
                .setSubject(user.getLogin())
                .setExpiration(refreshExpiration)
                .signWith(jwtRefreshSecret)
                .compact();
    }

    public boolean validateAccessToken(@Nonnull String accessToken){
        return validateToken(accessToken, jwtAccessSecret);
    }

    public boolean validateRefreshToken(@Nonnull String refreshToken){
        return validateToken(refreshToken, jwtRefreshSecret);
    }

    private boolean validateToken(String token, SecretKey jwtSecret) {
        try{
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            logger.info(e.getMessage());
        }
        return false;
    }

    public Claims getAccessClaims(@Nonnull String token) {
        return getClaims(token, jwtAccessSecret);
    }

    public Claims getRefreshClaims(@Nonnull String token) {
        return getClaims(token, jwtRefreshSecret);
    }

    private Claims  getClaims(@Nonnull String token, @Nonnull Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
