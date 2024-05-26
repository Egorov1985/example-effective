package ru.egorov.effectiveexample.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.egorov.effectiveexample.dto.MoneyTransferDto;
import ru.egorov.effectiveexample.exception.ResourceException;
import ru.egorov.effectiveexample.exception.UserNotFoundException;
import ru.egorov.effectiveexample.service.DepositServiceServiceImp;
import ru.egorov.effectiveexample.util.Constants;


@Tag(name = "Deposit Controller", description = "User bank account manager!")
@SecurityRequirement(name = Constants.SECURITY_SWAGGER)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/deposit")
public class DepositController {

    private final DepositServiceServiceImp depositServiceImp;

    @Operation(
            summary = "Перевод денег в системе между пользователями.",
            description = "На вход принимает номер телефона по которому находим пользваотеля на счет которого хотим перевети денежные средвства." +
                    "Если на балансе пользователя достаточно средств для трансфера, то пользователь получает уведомление о успешной операции." +
                    "В противном случае если не достачтоно средств или пользователь случайно пытаеться перевести себе же на счет то получает " +
                    "сообщение с соответствующим содержанием. Также будет сообщение об ошибке отправить несуществующему пользователю.",
            tags = "patch"
    )
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
                    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
                    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ResourceException.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
                    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = UserNotFoundException.class), mediaType = MediaType.APPLICATION_JSON_VALUE)})
            }
    )
    @PatchMapping("/transfer")
    public ResponseEntity<String> transferDeposit(@RequestBody @Valid MoneyTransferDto moneyTransferDto, @NonNull Authentication authentication) {
        if (depositServiceImp.transferMoneyToUser(authentication.getPrincipal().toString(), moneyTransferDto.getPhone(), moneyTransferDto.getDeposit()))
            return ResponseEntity.ok().body("Перевод денег успешно завершен.");
        return ResponseEntity.badRequest().body("Перевод завершился неудачей!");
    }
}
