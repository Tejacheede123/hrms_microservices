package com.teja.employee.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swaggerconfig {
   @Bean
    public OpenAPI employeeOpenAPI() {

        return new OpenAPI()

                .info(new Info()

                        .title("Employee Service API")

                        .version("1.0")

                        .description("Employee Management Microservice APIs")

                        .contact(new Contact()

                                .name("Teja")

                                .email("teja@gmail.com")))

                .externalDocs(new ExternalDocumentation()

                        .description("Employee Service Documentation"));
    }
}
