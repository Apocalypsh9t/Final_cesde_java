package com.materiales.materiales.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * Configuración global de:
 *  1. CORS — solo permite peticiones desde el puerto de React (5173)
 *  2. Swagger / OpenAPI — genera la documentación interactiva en /swagger-ui.html
 */
@Configuration
public class WebConfig {

    // ─────────────────────────────────────────────
    // CORS: Solo el frontend de React puede llamar al backend
    // ─────────────────────────────────────────────
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Orígenes permitidos: Vite/React (5173 y 3000)
        config.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:3000"));

        // Métodos HTTP permitidos
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Cabeceras permitidas (Content-Type es obligatorio para JSON)
        config.setAllowedHeaders(List.of("*"));

        // Permitir credenciales (cookies, etc.)
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta config a TODOS los endpoints /api/**
        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(source);
    }

    // ─────────────────────────────────────────────
    // Swagger / OpenAPI 3 — Información del proyecto
    // ─────────────────────────────────────────────
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cosmo Schools — API de Inventario")
                        .version("1.0.0")
                        .description(
                            "API REST para la gestión de materiales e inventario educativo. " +
                            "Permite a **Profesores** administrar el inventario y a **Estudiantes** " +
                            "consultar disponibilidad y realizar préstamos."
                        )
                        .contact(new Contact()
                                .name("Equipo Cosmo Schools")
                                .email("admin@cosmo.edu.co"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT"))
                )
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor local de desarrollo")
                ));
    }
}
