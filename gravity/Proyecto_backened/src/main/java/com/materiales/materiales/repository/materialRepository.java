package com.materiales.materiales.repository;

import com.materiales.materiales.model.material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface materialRepository extends JpaRepository<material, Integer> {
    // Spring genera el SQL automáticamente por el nombre del método
    List<material> findByName(String name);
}