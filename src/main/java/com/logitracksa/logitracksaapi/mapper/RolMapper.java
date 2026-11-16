package com.logitracksa.logitracksaapi.mapper;

import com.logitracksa.logitracksaapi.dto.request.RolDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.RolDtoResponse;
import com.logitracksa.logitracksaapi.model.Rol;
import org.springframework.stereotype.Component;

@Component
public class RolMapper {

    public RolDtoResponse entityToDto(Rol rol) {
        if (rol == null) return null;
        return new RolDtoResponse(rol.getId(), rol.getNombre());
    }

    public Rol dtoToEntity(RolDtoRequest dto) {
        if (dto == null) return null;
        Rol rol = new Rol();
        rol.setNombre(dto.nombre());
        return rol;
    }

    public void updateEntityFromDto(RolDtoRequest dto, Rol rol) {
        if (dto == null || rol == null) return;
        rol.setNombre(dto.nombre());
    }
}
