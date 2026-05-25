package com.materiales.materiales.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

// (Escritura / POST) - Lo que el cliente envía para registrar un usuario
@Data
@Schema(description = "Datos necesarios para registrar un nuevo usuario en el sistema")
public class usuarioRequestDTO {

    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String nombre;

    @Schema(description = "Número de documento de identidad", example = "123456789")
    private String documento;

    @Schema(description = "Correo electrónico institucional", example = "juan.perez@cosmo.edu.co")
    private String correo;

    @Schema(description = "Rol del usuario: ESTUDIANTE o PROFESOR", example = "ESTUDIANTE",
            allowableValues = {"ESTUDIANTE", "PROFESOR"})
    private String rol;
}
