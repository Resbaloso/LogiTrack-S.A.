package com.logitracksa.logitracksaapi.mapper;

import com.logitracksa.logitracksaapi.dto.request.BodegaDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.BodegaDtoResponse;
import com.logitracksa.logitracksaapi.dto.response.UsuarioDtoResponse;
import com.logitracksa.logitracksaapi.model.Bodega;
import com.logitracksa.logitracksaapi.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class BodegaMapper {

    public BodegaDtoResponse entityToDto(Bodega bodega, UsuarioDtoResponse usuarioDto) {
        if (bodega == null || usuarioDto == null) return null;
        return new BodegaDtoResponse(
                bodega.getId(),
                bodega.getNombre(),
                bodega.getUbicacion(),
                bodega.getCapacidad(),
                usuarioDto
        );
    }

    public Bodega dtoToEntity(BodegaDtoRequest dto, Usuario usuario) {
        if (dto == null || usuario == null) return null;
        Bodega bodega = new Bodega();
        bodega.setNombre(dto.nombre());
        bodega.setUbicacion(dto.ubicacion());
        bodega.setCapacidad(dto.capacidad());
        bodega.setUsuario(usuario);
        return bodega;
    }

    public void updateEntityFromDto(BodegaDtoRequest dto, Bodega bodega, Usuario usuario) {
        if (dto == null || bodega == null || usuario == null) return;
        bodega.setNombre(dto.nombre());
        bodega.setUbicacion(dto.ubicacion());
        bodega.setCapacidad(dto.capacidad());
        bodega.setUsuario(usuario);
    }
}
