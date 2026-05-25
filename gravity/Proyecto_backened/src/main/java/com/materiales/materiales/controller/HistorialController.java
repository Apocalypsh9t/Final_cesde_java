package com.materiales.materiales.controller;

import com.materiales.materiales.dto.HistorialResponseDTO;
import com.materiales.materiales.service.HistorialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para el Historial de Movimientos.
 *
 * Solo los usuarios con rol PROFESOR pueden acceder a estos endpoints.
 * Los ESTUDIANTES recibirán un error 403 (Forbidden).
 */
@RestController
@RequestMapping("/api/historial")
@Tag(name = "Historial", description = "Endpoints para consultar el historial de movimientos (solo profesores)")
public class HistorialController {

    @Autowired
    private HistorialService historialService;

    // ─────────────────────────────────────────────
    // GET /api/historial  → Obtener historial completo
    // Restringido a: PROFESOR
    // ─────────────────────────────────────────────
    @GetMapping
    @Operation(
        summary = "Obtener historial de movimientos",
        description = "Retorna el historial completo de movimientos de materiales. " +
                      "**Solo los Profesores** pueden acceder a este endpoint. " +
                      "Los estudiantes recibirán un error 403 (Forbidden)."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial obtenido correctamente",
            content = @Content(schema = @Schema(implementation = HistorialResponseDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acceso denegado: solo profesores pueden ver el historial"),
        @ApiResponse(responseCode = "400", description = "Falta el parámetro 'rol' en la query")
    })
    public ResponseEntity<?> obtenerHistorial(
            @RequestParam(name = "rol", required = true)
            @Parameter(description = "Rol del usuario (PROFESOR o ESTUDIANTE)", example = "PROFESOR")
            String rol) {

        if (rol == null || rol.isBlank()) {
            return ResponseEntity.badRequest().body("El parámetro 'rol' es requerido");
        }

        String rolUpper = rol.trim().toUpperCase();

        if (!rolUpper.equals("PROFESOR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado: solo profesores pueden ver el historial");
        }

        List<HistorialResponseDTO> historial = historialService.obtenerHistorial();
        return ResponseEntity.ok(historial);
    }
}
