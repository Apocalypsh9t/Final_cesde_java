package com.materiales.materiales.service;

import com.materiales.materiales.dto.HistorialResponseDTO;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class HistorialService {

    public List<HistorialResponseDTO> obtenerHistorial() {
        return Arrays.asList(
            new HistorialResponseDTO(1L, "Carlos Méndez", "Profesor", "PROF-48291", "carlos.mendez@cso.edu", "Retiró", "Laptop HP EliteBook", "2026-05-18"),
            new HistorialResponseDTO(2L, "Valentina Ríos", "Estudiante", "EST-19384", "valentina.rios@cso.edu", "Devolvió", "Tablet Samsung Galaxy Tab", "2026-05-17"),
            new HistorialResponseDTO(3L, "Julián Torres", "Profesor", "PROF-77821", "julian.torres@cso.edu", "Retiró", "Proyector Epson X200", "2026-05-16"),
            new HistorialResponseDTO(4L, "Sofía Herrera", "Estudiante", "EST-55290", "sofia.herrera@cso.edu", "Retiró", "Kit de robótica LEGO Education", "2026-05-15"),
            new HistorialResponseDTO(5L, "Andrés López", "Profesor", "PROF-66510", "andres.lopez@cso.edu", "Devolvió", "Microscopio Digital", "2026-05-14")
        );
    }
}
