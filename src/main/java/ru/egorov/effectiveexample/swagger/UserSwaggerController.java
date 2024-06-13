package ru.egorov.effectiveexample.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;
import ru.egorov.effectiveexample.dto.*;
import ru.egorov.effectiveexample.exception.ResourceException;
import ru.egorov.effectiveexample.exception.UserNotFoundException;

import java.util.List;

@Tag(name = "User Controller", description = "User manager controller!")
public interface UserSwaggerController {

    @Operation(
            summary = "Регистрация нового пользователя в системе.",
            description = "На вход принимает логин, пароль, начальный депозит, номер телефона и электронный адрес. Если логин," +
                    "номер телефона и электронный адрес не заняты то регистрирует нового пользователя в системе. Иначе возвращает ответ с ошибкой о регистрации.",
            tags = "post"
    )
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserView.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
                    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = WebExchangeBindException.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)})
            }
    )
    Mono<ResponseEntity<UserView>> createUser(UserRegistration userRegistration);


    @Operation(
            summary = "Добавление информации пользователи.",
            description = "На вход принимает ФИО пользователя и год его рождения.",
            tags = "patch"
    )
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserView.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
                    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ConstraintViolationException.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)})
            }
    )
    Mono<ResponseEntity<UserView>> addInfoUser(@RequestBody UserDtoInfo userDtoInfo, String login);


    @Operation(
            summary = "Добавление дополнительного номера телефона пользователя.",
            description = "На принимает номер телефона, если номера телефона не занят другим пользователем, то возращает сообщение об удачном добавлении.",
            tags = "patch"
    )
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
                    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ResourceException.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)})
            }
    )
    Mono<ResponseEntity<String>> addUserAdditionalPhone(String login, PhoneDto phone);


    @Operation(
            summary = "Добавление дополнительный электронной почты пользователя.",
            description = "На вход принимает адрес почты, если не занят другим пользователем, то возращает сообщение об удачном добавлении.",
            tags = "patch"
    )
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
                    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ResourceException.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)})
            }
    )
    Mono<ResponseEntity<String>> addUserAdditionalEmail(EmailDto email);

    @Operation(
            summary = "Удаление номера телефона пользователя.",
            description = "В запросе принимает номера телефона который необходимо удалить. Если у пользователя это последний номер телефона," +
                    "то пользователю приход сообщение о том, что нельзя удалить его.",
            tags = "delete"
    )
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
                    @ApiResponse(responseCode = "204", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
                    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ResourceException.class), mediaType = MediaType.APPLICATION_JSON_VALUE)})
            }
    )
    Mono<ResponseEntity<String>> deleteUserPhone(PhoneDto phone);

    @Operation(
            summary = "Удаление номера телефона пользователя.",
            description = "В запросе принимает адрес электроной почты который необходимо удалить. Если у пользователя это последний электронный ," +
                    "то пользователю приход сообщение о том, что нельзя его удалить.",
            tags = "delete"
    )
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
                    @ApiResponse(responseCode = "204", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
                    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ResourceException.class), mediaType = MediaType.APPLICATION_JSON_VALUE)})
            }
    )
    Mono<ResponseEntity<String>> deleteUserEmail(EmailDto email);

    @Operation(
            summary = "Поиск пользователя.",
            description = "В запросе принимает строку. После строка проверяетьтся на соответсвие что это почта, либо номер телефона, либо дата рождения" +
                    "пользвателя или же ФИО. " +
                    "Если передана дата рождения, то вернуться записи где дата рождения больше чем переданный в запросе. " +
                    "Если передан телефон, то вернет пользователя с данным номером телефона. " +
                    "Если передано ФИО, то совпадения в зависимости от того на сколько полный передан ФИО. Если передан полностью ФИО, то вернет по 100% сходству" +
                    "Если Фамилия и Имя, или же только Фамилия то только где есть совпадения по Имени и фамилии, либо только по фамилии." +
                    "Если передан email, то вернет пользователя с данной электронной почтой!" +
                    "Если же вдруг совпадений нет совсем, о вернеться сообщение о том что пользователи попадающие под поиск не найдены",

            tags = "get"
    )
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = List.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
                    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = UserNotFoundException.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            }
    )
    @GetMapping("/action/search")
    Mono<ResponseEntity<List<UserView>>> getUsers(String search);
}
