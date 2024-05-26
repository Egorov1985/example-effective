package ru.egorov.effectiveexample.controller;


import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.egorov.effectiveexample.exception.UserNotFoundException;
import ru.egorov.effectiveexample.jwt.AuthService;
import ru.egorov.effectiveexample.jwt.JwtRequest;
import ru.egorov.effectiveexample.jwt.JwtResponse;
import ru.egorov.effectiveexample.jwt.RefreshJwtRequest;
import ru.egorov.effectiveexample.util.Constants;

import javax.naming.AuthenticationException;


@Tag(name = "Authentication", description = "Jwt Token authentication manager!")
@SecurityRequirement(name = Constants.SECURITY_SWAGGER)
@RestController
@RequestMapping("api/auth/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @Operation(
            summary = "Авторизация пользователя в системе. С последующим получения access и refresh токена.",
            description = "Получение access и refresh токена.",
            tags = "post"
    )
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", content = {@Content (schema = @Schema (implementation = JwtResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
                    @ApiResponse(responseCode = "404", content = {@Content (schema = @Schema (implementation = UserNotFoundException.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
                    @ApiResponse(responseCode = "400", content = {@Content (schema = @Schema (implementation = AuthenticationException.class), mediaType = MediaType.APPLICATION_JSON_VALUE)})
            }
    )
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> sighIn(@RequestBody @Valid JwtRequest authRequest) throws AuthenticationException {
        JwtResponse response = authService.login(authRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Получение нового access токена.",
            description = "На вход принимает refresh токен, если токен валиден то возвращает новый access токен.",
            tags = "post"
    )
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", content = {@Content (schema = @Schema (implementation = JwtResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
                    @ApiResponse(responseCode = "400", content = {@Content (schema = @Schema (implementation = JwtException.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            }
    )
    @PostMapping("/token")
    public ResponseEntity<JwtResponse> updateAccessToken(@RequestBody @Valid RefreshJwtRequest refreshJwtRequest) throws AuthenticationException {
        JwtResponse response = authService.getAccessToken(refreshJwtRequest.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Получение нового access и refresh токена.",
            description = "На вход принимает refresh токен, если токен валиден то возвращает новый access и refresh токен.",
            tags = "post"
    )
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", content = {@Content (schema = @Schema (implementation = JwtResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
                    @ApiResponse(responseCode = "400", content = {@Content (schema = @Schema (implementation = JwtException.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            }
    )
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.newRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }
}
