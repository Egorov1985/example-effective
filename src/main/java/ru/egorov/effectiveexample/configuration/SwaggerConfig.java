package ru.egorov.effectiveexample.configuration;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class SwaggerConfig {


    @Value(value = "${ru.egorov.dev-url}")
    private static String devUrl;


    @Bean
    public OpenAPI setOpenApi() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Test swagger!");
        Contact contact = new Contact();
        contact.setEmail("san_egorov@mail.ru");
        contact.setName("Александр");
        contact.setUrl("https://t.me/@Alexander_atlan");
        License license = new License()
                .name("MIT Licence")
                .url("https://choosealicense.com/licence/mit/");
        Info info = new Info()
                .title("Rest Example Api")
                .version("1.0")
                .contact(contact)
                .description("Example for Effective Mobile.")
                .license(license);
        final String securitySchemaName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemaName))
                .components(new Components().addSecuritySchemes(securitySchemaName, new SecurityScheme().name(securitySchemaName).type(SecurityScheme.Type.HTTP)
                        .bearerFormat("JWT")))
                .info(info)
                .servers(List.of(devServer));
    }
}
