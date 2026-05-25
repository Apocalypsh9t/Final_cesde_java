package com.materiales.materiales.service;

import com.materiales.materiales.dto.materialRequestDTO;
import com.materiales.materiales.dto.materialResponseDTO;
import com.materiales.materiales.exception.ResourceNotFoundException;
import com.materiales.materiales.mapper.materialMapper;
import com.materiales.materiales.model.material;
import com.materiales.materiales.model.usuario;
import com.materiales.materiales.repository.materialRepository;
import com.materiales.materiales.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MaterialService {

    @Autowired
    private materialRepository materialRepository;

    @Autowired
    private usuarioRepository usuarioRepository;

    @Autowired
    private materialMapper materialMapper;

    public List<materialResponseDTO> obtenerTodos() {
        List<material> materiales = materialRepository.findAll();
        return materialMapper.toResponseDTOList(materiales);
    }

    public materialResponseDTO guardar(materialRequestDTO requestDTO) {
        material mat = materialMapper.toEntity(requestDTO);
        if (requestDTO.getUsuarioId() != null) {
            usuario user = usuarioRepository.findById(requestDTO.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Usuario con ID " + requestDTO.getUsuarioId() + " no encontrado"
                ));
            mat.setUsuario(user);
        }
        material guardado = materialRepository.save(mat);
        return materialMapper.toResponseDTO(guardado);
    }

    public materialResponseDTO buscarPorId(int id) {
        material mat = materialRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Material con ID " + id + " no encontrado"
            ));
        return materialMapper.toResponseDTO(mat);
    }
}