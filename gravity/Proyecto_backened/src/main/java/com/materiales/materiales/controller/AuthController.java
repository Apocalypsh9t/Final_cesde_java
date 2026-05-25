package com.materiales.materiales.controller;

import com.materiales.materiales.dto.LoginRequestDTO;
import com.materiales.materiales.dto.RoleVerificationResponseDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para inicio de sesión, registro y verificación de roles")
public class AuthController {

    @Autowired
    private usuarioRepository usuarioRepository;

    @Autowired
    private usuarioMapper usuarioMapper;

    // ─────────────────────────────────────────────
    // POST /api/auth/login  → Iniciar sesión
    // ─────────────────────────────────────────────
    @PostMapping("/login")
    @Operation(
        summary = "Iniciar sesión en el sistema",
        description = "Valida el correo (o documento) como usuario y el documento como contraseña. Retorna los datos del usuario si es exitoso."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autenticación exitosa",
            content = @Content(schema = @Schema(implementation = usuarioResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Usuario o contraseña incorrectos")
    })
    public ResponseEntity<usuarioResponseDTO> login(
            @RequestBody LoginRequestDTO loginRequest) {

        if (loginRequest.getUsuario() == null || loginRequest.getContrasena() == null) {
            return ResponseEntity.badRequest().build();
        }

        String inputUsuario = loginRequest.getUsuario().trim().toLowerCase();
        String inputContrasena = loginRequest.getContrasena().trim();

        List<usuario> usuarios = usuarioRepository.findAll();
        usuario usuarioEncontrado = null;

        for (usuario u : usuarios) {
            boolean correoMatches = u.getCorreo() != null && u.getCorreo().trim().toLowerCase().equals(inputUsuario);
            boolean documentoAsUserMatches = u.getDocumento() != null && u.getDocumento().trim().equals(inputUsuario);
            boolean contrasenaMatches = u.getDocumento() != null && u.getDocumento().trim().equals(inputContrasena);

            if ((correoMatches || documentoAsUserMatches) && contrasenaMatches) {
                usuarioEncontrado = u;
                break;
            }
        }

        if (usuarioEncontrado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        usuarioResponseDTO dto = usuarioMapper.toResponseDTO(usuarioEncontrado);
        return ResponseEntity.ok(dto);
    }

    // ─────────────────────────────────────────────
    // POST /api/auth/register  → Registrar un usuario
    // ─────────────────────────────────────────────
    @PostMapping("/register")
    @Operation(
        summary = "Registrar un nuevo usuario",
        description = "Crea un usuario nuevo con rol ESTUDIANTE o PROFESOR. Reutiliza el DTO de request."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente",
            content = @Content(schema = @Schema(implementation = usuarioResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<usuarioResponseDTO> register(
            @RequestBody usuarioRequestDTO requestDTO) {

        usuario nuevoUsuario = new usuario();
        nuevoUsuario.setNombre(requestDTO.getNombre());
        nuevoUsuario.setDocumento(requestDTO.getDocumento());
        nuevoUsuario.setCorreo(requestDTO.getCorreo());

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

    // ─────────────────────────────────────────────
    // GET /api/auth/verify/{id}  → Verificar rol del usuario
    // ─────────────────────────────────────────────
    @GetMapping("/verify/{id}")
    @Operation(
        summary = "Verificar el rol de un usuario",
        description = "Retorna el rol del usuario especificado por su ID y determina si es Administrador (Profesor) o Estudiante."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verificación exitosa",
            content = @Content(schema = @Schema(implementation = RoleVerificationResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<RoleVerificationResponseDTO> verify(
            @PathVariable 
            @Parameter(description = "ID del usuario a verificar") Long id) {

        return usuarioRepository.findById(id)
                .map(u -> {
                    RoleVerificationResponseDTO response = new RoleVerificationResponseDTO();
                    response.setRol(u.getRol());
                    response.setAdmin("PROFESOR".equalsIgnoreCase(u.getRol()));
                    response.setStudent("ESTUDIANTE".equalsIgnoreCase(u.getRol()));
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
