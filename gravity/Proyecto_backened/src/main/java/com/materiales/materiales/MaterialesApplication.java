package com.materiales.materiales;

import com.materiales.materiales.dto.materialRequestDTO;
import com.materiales.materiales.dto.materialResponseDTO;
import com.materiales.materiales.model.usuario;
import com.materiales.materiales.repository.usuarioRepository;
import com.materiales.materiales.service.MaterialService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MaterialesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaterialesApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(
            MaterialService materialService,
            usuarioRepository usuarioRepository) {

        return args -> {
            System.out.println("--- Agregando Datos Manualmente ---");

            // 1. Crear y guardar usuarios con sus roles
            usuario u1 = new usuario();
            u1.setNombre("daniel");
            u1.setDocumento("6776");
            u1.setCorreo("voicaloid.suicidio@cosmo.edu.co");
            u1.setRol("ESTUDIANTE");
            usuarioRepository.save(u1);

            usuario u2 = new usuario();
            u2.setNombre("juanpepe");
            u2.setDocumento("12345");
            u2.setCorreo("pablo.nacional@cosmo.edu.co");
            u2.setRol("PROFESOR");
            usuarioRepository.save(u2);

            // 2. Crear un material semilla asociado al estudiante Juan
            materialRequestDTO request = new materialRequestDTO();
            request.setName("Televisor de Sala");
            request.setCondition("Bueno");
            request.setDate("2026-05-01");
            request.setReturndate("2026-05-15");
            request.setUsuarioId(u1.getIdUsuario());

            materialResponseDTO response = materialService.guardar(request);

            System.out.println("--- MATERIAL GUARDADO ---");
            System.out.println("ID: " + response.getId());
            System.out.println("Nombre: " + response.getName());
            System.out.println("Condicion: " + response.getCondition());
        };
    }}