package ru.egorov.effectiveexample.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.egorov.effectiveexample.controller.swagger.AuthControllerSwagger;
import ru.egorov.effectiveexample.jwt.AuthService;
import ru.egorov.effectiveexample.jwt.JwtRequest;
import ru.egorov.effectiveexample.jwt.JwtResponse;
import ru.egorov.effectiveexample.jwt.RefreshJwtRequest;
import ru.egorov.effectiveexample.util.Constants;

import javax.naming.AuthenticationException;



@RestController
@RequestMapping("api/auth/v1")
@RequiredArgsConstructor
public class AuthController implements AuthControllerSwagger {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> sighIn(@RequestBody @Valid JwtRequest authRequest) throws AuthenticationException {
        JwtResponse response = authService.login(authRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> updateAccessToken(@RequestBody @Valid RefreshJwtRequest refreshJwtRequest)   {
        JwtResponse response = authService.getAccessToken(refreshJwtRequest.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.newRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }
}
