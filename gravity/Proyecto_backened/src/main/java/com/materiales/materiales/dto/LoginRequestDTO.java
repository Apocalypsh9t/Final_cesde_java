package com.materiales.materiales.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos requeridos para iniciar sesión")
public class LoginRequestDTO {

    @Schema(description = "Correo electrónico institucional o Documento del usuario", example = "juan.estudiante@cosmo.edu.co")
    private String usuario;

    @Schema(description = "Número de documento del usuario (actúa como contraseña)", example = "100200300")
    private String contrasena;
}
