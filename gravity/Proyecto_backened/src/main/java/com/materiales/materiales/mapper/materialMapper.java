package com.materiales.materiales.mapper;

import com.materiales.materiales.dto.materialRequestDTO;
import com.materiales.materiales.dto.materialResponseDTO;
import com.materiales.materiales.model.material;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface materialMapper {

    // DE ENTIDAD A DTO (para responderle al cliente)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "condition", source = "condition")
    @Mapping(target = "nombreUsuario", source = "usuario.nombre")
    materialResponseDTO toResponseDTO(material mat);

    List<materialResponseDTO> toResponseDTOList(List<material> materiales);

    // DE DTO A ENTIDAD (para guardar en la base de datos)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    material toEntity(materialRequestDTO requestDTO);
}