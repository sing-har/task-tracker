package com.example.tasktracker.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI taskTrackerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                    .title("Task Tracker API")
                    .description("CRUD API for managing tasks")
                    .version("1.0.0"));
    }
}
