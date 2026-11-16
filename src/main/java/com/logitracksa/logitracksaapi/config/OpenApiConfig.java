package com.logitracksa.logitracksaapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("API DOCUMENTADA LOGITRACK S.A.")
                        .version("1.0")
                        .description("DOCUMENTACION OFICIAL DEL SISTEMA DE MANEJO DE BODEGAS PARA LA EMPRESA LOGITRACK S.A."));
    }
}
