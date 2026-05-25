package com.materiales.materiales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HistorialResponseDTO {
    private Long id;
    private String nombre;
    private String rol;
    private String documento;
    private String correo;
    private String accion;
    private String objeto;
    private String fecha;
}
