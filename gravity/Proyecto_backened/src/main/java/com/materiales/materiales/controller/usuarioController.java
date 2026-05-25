package com.materiales.materiales.controller;

import com.materiales.materiales.dto.usuarioRequestDTO;
import com.materiales.materiales.dto.usuarioResponseDTO;
import com.materiales.materiales.mapper.usuarioMapper;
import com.materiales.materiales.model.usuario;
import com.materiales.materiales.repository.usuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Controlador REST para la gestión de Usuarios.
 *
 * Rol PROFESOR (Admin): puede ver todos los usuarios.
 * Rol ESTUDIANTE:       puede registrarse en el sistema.
 *
 * Solo acepta peticiones del origen React (http://localhost:5173),
 * configurado globalmente en WebConfig.java.
 */
@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Endpoints para listar y registrar usuarios (Profesores y Estudiantes)")
public class usuarioController {

    @Autowired
    private usuarioRepository usuarioRepository;

    @Autowired
    private usuarioMapper usuarioMapper;

    // ─────────────────────────────────────────────
    // GET /api/usuarios  → Listar todos (uso del Profesor/Admin)
    // ─────────────────────────────────────────────
    @GetMapping
    @Operation(
        summary = "Listar todos los usuarios",
        description = "Retorna una lista de todos los usuarios registrados junto con su rol. " +
                      "Pensado para el panel de administración del Profesor."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente",
            content = @Content(schema = @Schema(implementation = usuarioResponseDTO.class)))
    })
    public ResponseEntity<List<usuarioResponseDTO>> obtenerTodos() {
        List<usuario> usuarios = usuarioRepository.findAll();
        List<usuarioResponseDTO> dtos = usuarioMapper.toResponseDTOList(usuarios);
        return ResponseEntity.ok(dtos);
    }



    

    // ─────────────────────────────────────────────
    // GET /api/usuarios/{id}  → Buscar usuario por ID
    // ─────────────────────────────────────────────
    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar un usuario por su ID",
        description = "Retorna los datos de un usuario específico según su ID. " +
                      "Devuelve 404 si el usuario no existe."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado",
            content = @Content(schema = @Schema(implementation = usuarioResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<usuarioResponseDTO> buscarPorId(
            @PathVariable
            @Parameter(description = "ID numérico del usuario", example = "1")
            Long id) {

        return usuarioRepository.findById(id)
                .map(u -> ResponseEntity.ok(usuarioMapper.toResponseDTO(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ─────────────────────────────────────────────
    // POST /api/usuarios  → Registrar un nuevo usuario
    // ─────────────────────────────────────────────
    @PostMapping
    @Operation(
        summary = "Registrar un nuevo usuario",
        description = "Crea un usuario nuevo con rol ESTUDIANTE o PROFESOR. " +
                      "Si no se indica rol, se asigna ESTUDIANTE por defecto. " +
                      "Usa el DTO de request para no exponer la entidad directamente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado correctamente",
            content = @Content(schema = @Schema(implementation = usuarioResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<usuarioResponseDTO> guardar(
            @RequestBody
            @Parameter(description = "Datos del nuevo usuario a registrar")
            usuarioRequestDTO requestDTO) {

        // Construimos la entidad desde el DTO (sin exponer la entidad al cliente)
        usuario nuevoUsuario = new usuario();
        nuevoUsuario.setNombre(requestDTO.getNombre());
        nuevoUsuario.setDocumento(requestDTO.getDocumento());
        nuevoUsuario.setCorreo(requestDTO.getCorreo());

        // Validación del rol: solo ESTUDIANTE o PROFESOR, ESTUDIANTE por defecto
        String rol = requestDTO.getRol();
        if (rol == null || rol.isBlank()) {
            nuevoUsuario.setRol("ESTUDIANTE");
        } else {
            String rolUpper = rol.toUpperCase().trim();
            if (!rolUpper.equals("ESTUDIANTE") && !rolUpper.equals("PROFESOR")) {
                return ResponseEntity.badRequest().build();
            }
            nuevoUsuario.setRol(rolUpper);
        }

        usuario guardado = usuarioRepository.save(nuevoUsuario);
        usuarioResponseDTO dto = usuarioMapper.toResponseDTO(guardado);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
