package ru.egorov.effectiveexample.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.Nonnull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egorov.effectiveexample.exception.UserNotFoundException;
import ru.egorov.effectiveexample.model.Token;
import ru.egorov.effectiveexample.model.User;
import ru.egorov.effectiveexample.repository.TokenRepository;
import ru.egorov.effectiveexample.repository.UserRepository;

import javax.naming.AuthenticationException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public JwtResponse login(@Nonnull JwtRequest authRequest) throws AuthenticationException {
        final User user = getUser(authRequest.getLogin());
        if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            final String accessToken = jwtProvider.generatedAccessToken(user);
            final String refreshToken = jwtProvider.generatedRefreshToken(user);
            tokenRepository.save(Token.builder().token(refreshToken).isActive(true).login(user.getLogin()).build());
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthenticationException("Неверный логин и пароль!");
        }
    }

    public JwtResponse getAccessToken(@Nonnull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = tokenRepository.findByToken(refreshToken).orElseThrow(() -> new JwtException("Invalid token!")).getToken();
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = getUser(login);
                final String accessToken = jwtProvider.generatedAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        throw new JwtException("Invalid token!");
    }

    @Transactional
    public JwtResponse newRefreshToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            Token token = tokenRepository.findByToken(refreshToken).orElseThrow(() -> new JwtException("Invalid token!"));
            final String saveRefreshToken = token.getToken();
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken) && token.isActive()) {
                User user = getUser(login);
                final String accessToken = jwtProvider.generatedAccessToken(user);
                final String newRefreshToken = jwtProvider.generatedRefreshToken(user);
                token.setActive(false);
                tokenRepository.save(token);
                tokenRepository.save(Token.builder().token(newRefreshToken).login(login).isActive(true).build());
                return new JwtResponse(accessToken, refreshToken);
            }
        }
        throw new JwtException("Invalid token!");
    }


    private User getUser(String login) {
        return userRepository.findUserByLogin(login).orElseThrow(() -> new UserNotFoundException("User not found!"));
    }
}
