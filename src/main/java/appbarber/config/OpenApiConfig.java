package appbarber.config;


import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI barberiaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Barbería – Gestión de Turnos")
                        .description("API para administrar empresas, barberos, servicios y turnos.")
                        .version("v1.0.0")
                        .contact(new Contact().name("Soporte").email("soporte@barberia.local")))
                .servers(List.of(
                        new Server().url("http://localhost:8000").description("Local Dev")
                ))
                .externalDocs(new ExternalDocumentation()
                        .description("Repositorio / Notas")
                        .url("https://example.com")); // opcional
    }
}
