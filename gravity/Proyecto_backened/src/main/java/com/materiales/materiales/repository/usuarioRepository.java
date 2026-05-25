package com.materiales.materiales.repository;

import com.materiales.materiales.model.usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface usuarioRepository extends JpaRepository<usuario, Long> {
    List<usuario> findByNombre(String nombre);
}