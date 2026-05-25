package com.materiales.materiales.dto;

import lombok.Data;

@Data
public class usuarioResponseDTO {
    private Long idUsuario;
    private String nombre;
    private String correo;
    private String rol;

}