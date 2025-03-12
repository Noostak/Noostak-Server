package org.noostak.global.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        servers = {
                @Server(url = "https://www.noostak-official.kro.kr", description = "prod server url"),
                @Server(url = "http://52.79.61.243", description = "staging server url"),
                @Server(url = "http://localhost:8080", description = "local server url")
        }
)
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("noostak Swagger")
                .description("noostak swagger ver")
                .version("1.0.0");
    }
}
