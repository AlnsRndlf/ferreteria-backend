package cl.duocuc.ferreteria_backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
    title = "Ferretería Los Maestros API",
    version = "1.0",
    description = "Catálogo, inventario y boletas de la ferretería"
))
public class OpenApiConfig {}
