package com.logitracksa.logitracksaapi.mapper;

import com.logitracksa.logitracksaapi.dto.request.UsuarioDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.UsuarioDtoResponse;
import com.logitracksa.logitracksaapi.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioDtoResponse entityToDto(Usuario usuario) {
        if (usuario == null) return null;
        return new UsuarioDtoResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellidos(),
                usuario.getEmail(),
                usuario.isActivo()
        );
    }

    public Usuario dtoToEntity(UsuarioDtoRequest dto) {
        if (dto == null) return null;
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.nombre());
        usuario.setApellidos(dto.apellidos());
        usuario.setEmail(dto.email());
        usuario.setActivo(dto.activo());
        return usuario;
    }

    public void updateEntityFromDto(UsuarioDtoRequest dto, Usuario usuario) {
        if (dto == null || usuario == null) return;
        usuario.setNombre(dto.nombre());
        usuario.setApellidos(dto.apellidos());
        usuario.setEmail(dto.email());
        usuario.setActivo(dto.activo());
    }
}
