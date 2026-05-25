package com.materiales.materiales.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Respuesta de la verificación de rol de un usuario")
public class RoleVerificationResponseDTO {

    @Schema(description = "Rol del usuario (ESTUDIANTE o PROFESOR)", example = "ESTUDIANTE")
    private String rol;

    @Schema(description = "Verdadero si el usuario tiene rol PROFESOR", example = "false")
    private boolean isAdmin;

    @Schema(description = "Verdadero si el usuario tiene rol ESTUDIANTE", example = "true")
    private boolean isStudent;
}
