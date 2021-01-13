package com.highcrit.ffacheckers.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "FFA-Checkers API", version = "v1"))
public class OpenApiConfiguration {}
