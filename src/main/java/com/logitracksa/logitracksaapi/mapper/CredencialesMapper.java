package com.logitracksa.logitracksaapi.mapper;

import com.logitracksa.logitracksaapi.dto.request.CredencialesDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.CredencialesDtoResponse;
import com.logitracksa.logitracksaapi.dto.response.RolDtoResponse;
import com.logitracksa.logitracksaapi.dto.response.UsuarioDtoResponse;
import com.logitracksa.logitracksaapi.model.Credenciales;
import com.logitracksa.logitracksaapi.model.Rol;
import com.logitracksa.logitracksaapi.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class CredencialesMapper {

    public CredencialesDtoResponse entityToDto(Credenciales credenciales, UsuarioDtoResponse usuarioDto, RolDtoResponse rolDto) {
        if (credenciales == null || usuarioDto == null || rolDto == null) return null;
        return new CredencialesDtoResponse(
                credenciales.getId(),
                credenciales.getUsername(),
                usuarioDto,
                rolDto
        );
    }

    public Credenciales dtoToEntity(CredencialesDtoRequest dto, Usuario usuario, Rol rol) {
        if (dto == null || usuario == null || rol == null) return null;
        Credenciales credenciales = new Credenciales();
        credenciales.setUsername(dto.username());
        credenciales.setPassword(dto.password());
        credenciales.setUsuario(usuario);
        credenciales.setRol(rol);
        return credenciales;
    }

    public void updateEntityFromDto(CredencialesDtoRequest dto, Credenciales credenciales, Usuario usuario, Rol rol) {
        if (dto == null || credenciales == null || usuario == null || rol == null) return;
        credenciales.setUsername(dto.username());
        credenciales.setPassword(dto.password());
        credenciales.setUsuario(usuario);
        credenciales.setRol(rol);
    }
}
