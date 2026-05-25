package com.materiales.materiales.dto;

import lombok.Data;

// (Escritura / POST) - Lo que el cliente envía
@Data
public class materialRequestDTO {
    private String name;
    private String condition;
    private String date;
    private String returndate;
    private Long usuarioId; // Solo el ID del usuario, no el objeto completo
}