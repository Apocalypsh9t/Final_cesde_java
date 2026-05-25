package com.materiales.materiales.mapper;

import com.materiales.materiales.dto.usuarioResponseDTO;
import com.materiales.materiales.model.usuario;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface usuarioMapper {

    usuarioResponseDTO toResponseDTO(usuario user);

    List<usuarioResponseDTO> toResponseDTOList(List<usuario> usuarios);
}
