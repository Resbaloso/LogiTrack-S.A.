package com.logitracksa.logitracksaapi.mapper;

import com.logitracksa.logitracksaapi.dto.request.AuditoriaDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.AuditoriaDtoResponse;
import com.logitracksa.logitracksaapi.dto.response.UsuarioDtoResponse;
import com.logitracksa.logitracksaapi.model.Auditoria;
import com.logitracksa.logitracksaapi.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class AuditoriaMapper {

    public AuditoriaDtoResponse entityToDto(Auditoria auditoria, UsuarioDtoResponse usuarioDto) {
        if (auditoria == null || usuarioDto == null) return null;
        return new AuditoriaDtoResponse(
                auditoria.getId(),
                auditoria.getTipoOperacion(),
                auditoria.getFecha(),
                usuarioDto,
                auditoria.getEntidadAfectada(),
                auditoria.getValoresAnteriores(),
                auditoria.getValoresNuevos()
        );
    }

    public Auditoria dtoToEntity(AuditoriaDtoRequest dto, Usuario usuario) {
        if (dto == null || usuario == null) return null;
        Auditoria auditoria = new Auditoria();
        auditoria.setTipoOperacion(dto.tipoOperacion());
        auditoria.setFecha(dto.fecha());
        auditoria.setUsuario(usuario);
        auditoria.setEntidadAfectada(dto.entidadAfectada());
        auditoria.setValoresAnteriores(dto.valoresAnteriores());
        auditoria.setValoresNuevos(dto.valoresNuevos());
        return auditoria;
    }

    public void updateEntityFromDto(AuditoriaDtoRequest dto, Auditoria auditoria, Usuario usuario) {
        if (dto == null || auditoria == null || usuario == null) return;
        auditoria.setTipoOperacion(dto.tipoOperacion());
        auditoria.setFecha(dto.fecha());
        auditoria.setUsuario(usuario);
        auditoria.setEntidadAfectada(dto.entidadAfectada());
        auditoria.setValoresAnteriores(dto.valoresAnteriores());
        auditoria.setValoresNuevos(dto.valoresNuevos());
    }
}
