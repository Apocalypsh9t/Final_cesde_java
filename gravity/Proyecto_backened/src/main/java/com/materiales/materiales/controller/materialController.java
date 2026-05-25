package com.materiales.materiales.controller;

import com.materiales.materiales.dto.materialRequestDTO;
import com.materiales.materiales.dto.materialResponseDTO;
import com.materiales.materiales.service.MaterialService;
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
 * Controlador REST para la gestión del Inventario de Materiales.
 *
 * Rol PROFESOR (Admin): puede crear materiales (POST).
 * Rol ESTUDIANTE:       puede consultar el inventario (GET) y ver dónde está un material.
 *
 * Las restricciones de rol son aplicadas en la capa de lógica de negocio /
 * en el frontend según el rol del usuario autenticado.
 *
 * Solo acepta peticiones del origen React (http://localhost:5173),
 * configurado globalmente en WebConfig.java.
 */
@RestController
@RequestMapping("/api/materiales")
@Tag(name = "Materiales", description = "Endpoints para la administración y consulta del inventario de materiales")
public class materialController {

    @Autowired
    private MaterialService materialService;

    // ─────────────────────────────────────────────
    // GET /api/materiales  → Listar todo el inventario
    // Disponible para: ESTUDIANTE y PROFESOR
    // ─────────────────────────────────────────────
    @GetMapping
    @Operation(
        summary = "Listar todos los materiales del inventario",
        description = "Retorna la lista completa de materiales registrados. " +
                      "Disponible para Estudiantes (solo lectura) y Profesores."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente",
            content = @Content(schema = @Schema(implementation = materialResponseDTO.class)))
    })
    public ResponseEntity<List<materialResponseDTO>> obtenerTodos() {
        List<materialResponseDTO> lista = materialService.obtenerTodos();
        return ResponseEntity.ok(lista);
    }

    // ─────────────────────────────────────────────
    // GET /api/materiales/{id}  → Buscar un material por ID
    // Disponible para: ESTUDIANTE y PROFESOR
    // ─────────────────────────────────────────────
    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar un material por su ID",
        description = "Retorna la información detallada de un material específico. " +
                      "Útil para que el Estudiante vea dónde se encuentra un ítem."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Material encontrado"),
        @ApiResponse(responseCode = "404", description = "Material no encontrado")
    })
    public ResponseEntity<materialResponseDTO> buscarPorId(
            @PathVariable
            @Parameter(description = "ID numérico del material", example = "1")
            int id) {
        materialResponseDTO mat = materialService.buscarPorId(id);
        return ResponseEntity.ok(mat);
    }

    // ─────────────────────────────────────────────
    // POST /api/materiales  → Registrar un nuevo material
    // Restringido a: PROFESOR (Admin)
    // ─────────────────────────────────────────────
    @PostMapping
    @Operation(
        summary = "Registrar un nuevo material en el inventario",
        description = "Crea un nuevo elemento en el inventario. " +
                      "**Solo los Profesores** pueden usar este endpoint. " +
                      "Se puede asociar opcionalmente a un usuario mediante su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Material creado correctamente",
            content = @Content(schema = @Schema(implementation = materialResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "El usuario indicado no existe")
    })
    public ResponseEntity<materialResponseDTO> guardar(
            @RequestBody
            @Parameter(description = "Datos del nuevo material a registrar")
            materialRequestDTO requestDTO) {
        materialResponseDTO guardado = materialService.guardar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }
}
