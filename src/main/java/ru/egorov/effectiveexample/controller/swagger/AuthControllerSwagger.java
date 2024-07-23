package ru.egorov.effectiveexample.controller.swagger;

import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.egorov.effectiveexample.exception.UserNotFoundException;
import ru.egorov.effectiveexample.jwt.JwtRequest;
import ru.egorov.effectiveexample.jwt.JwtResponse;
import ru.egorov.effectiveexample.jwt.RefreshJwtRequest;
import ru.egorov.effectiveexample.util.Constants;

import javax.naming.AuthenticationException;

@Tag(name = "Authentication", description = "Jwt Token authentication manager!")
@SecurityRequirement(name = Constants.SECURITY_SWAGGER)
public interface AuthControllerSwagger {

    @Operation(
            summary = "Авторизация пользователя в системе. С последующим получения access и refresh токена.",
            description = "Получение access и refresh токена.",
            tags = "post"
    )
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = JwtResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
                    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = UserNotFoundException.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
                    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = AuthenticationException.class), mediaType = MediaType.APPLICATION_JSON_VALUE)})
            }
    )
    @PostMapping("/login")
    ResponseEntity<JwtResponse> sighIn(@RequestBody @Valid JwtRequest authRequest) throws AuthenticationException;

    @Operation(
            summary = "Получение нового access токена.",
            description = "На вход принимает refresh токен, если токен валиден то возвращает новый access токен.",
            tags = "post"
    )
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = JwtResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
                    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = JwtException.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            }
    )
    @PostMapping("/token")
    ResponseEntity<JwtResponse> updateAccessToken(@RequestBody @Valid RefreshJwtRequest refreshJwtRequest);


    @Operation(
            summary = "Получение нового access и refresh токена.",
            description = "На вход принимает refresh токен, если токен валиден то возвращает новый access и refresh токен.",
            tags = "post"
    )
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = JwtResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
                    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = JwtException.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            }
    )
    @PostMapping("/refresh")
    ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request);

}
