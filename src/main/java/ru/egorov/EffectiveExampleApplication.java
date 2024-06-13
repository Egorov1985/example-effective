package ru.egorov;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "Example web flux",
        version = "1.0",
        description = "Sample documents"
))
public class EffectiveExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(EffectiveExampleApplication.class, args);
    }

}
