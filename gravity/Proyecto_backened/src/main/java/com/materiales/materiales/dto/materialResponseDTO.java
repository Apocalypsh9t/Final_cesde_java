package com.materiales.materiales.dto;

import lombok.Data;

// (Lectura / GET) - Lo que tú devuelves
@Data
public class materialResponseDTO {
    private int id;
    private String name;
    private String condition;
    private String nombreUsuario;
 
}