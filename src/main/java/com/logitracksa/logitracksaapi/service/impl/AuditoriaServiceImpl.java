package com.logitracksa.logitracksaapi.service.impl;

import com.logitracksa.logitracksaapi.dto.request.AuditoriaDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.AuditoriaDtoResponse;
import com.logitracksa.logitracksaapi.dto.response.UsuarioDtoResponse;
import com.logitracksa.logitracksaapi.exception.BusinessRuleException;
import com.logitracksa.logitracksaapi.exception.DatabaseRuleException;
import com.logitracksa.logitracksaapi.mapper.AuditoriaMapper;
import com.logitracksa.logitracksaapi.mapper.UsuarioMapper;
import com.logitracksa.logitracksaapi.model.Auditoria;
import com.logitracksa.logitracksaapi.model.Usuario;
import com.logitracksa.logitracksaapi.repository.AuditoriaRepository;
import com.logitracksa.logitracksaapi.repository.UsuarioRepository;
import com.logitracksa.logitracksaapi.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AuditoriaServiceImpl implements AuditoriaService {

    private final AuditoriaMapper auditoriaMapper;
    private final AuditoriaRepository auditoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    public AuditoriaDtoResponse guardar(AuditoriaDtoRequest dto) {
        if (!dto.tipoOperacion().equals("INSERT") &&
                !dto.tipoOperacion().equals("UPDATE") &&
                !dto.tipoOperacion().equals("DELETE")) {
            throw new BusinessRuleException("El tipo de operación debe ser INSERT, UPDATE o DELETE");
        }
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new DatabaseRuleException("No existe el usuario con id: " + dto.usuarioId()));
        Auditoria auditoria = auditoriaMapper.dtoToEntity(dto, usuario);
        UsuarioDtoResponse usuarioDto = usuarioMapper.entityToDto(usuario);
        return auditoriaMapper.entityToDto(auditoriaRepository.save(auditoria), usuarioDto);
    }

    @Override
    public AuditoriaDtoResponse actualizar(AuditoriaDtoRequest dto, Long id) {
        Auditoria auditoria = auditoriaRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el registro de auditoría con id: " + id));
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new DatabaseRuleException("No existe el usuario con id: " + dto.usuarioId()));
        auditoriaMapper.updateEntityFromDto(dto, auditoria, usuario);
        UsuarioDtoResponse usuarioDto = usuarioMapper.entityToDto(usuario);
        return auditoriaMapper.entityToDto(auditoriaRepository.save(auditoria), usuarioDto);
    }

    @Override
    public void eliminar(Long id) {
        Auditoria auditoria = auditoriaRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el registro de auditoría con id: " + id));
        auditoriaRepository.delete(auditoria);
    }

    @Override
    public List<AuditoriaDtoResponse> listar() {
        return auditoriaRepository.findAll().stream()
                .map(a -> auditoriaMapper.entityToDto(a, usuarioMapper.entityToDto(a.getUsuario())))
                .toList();
    }

    @Override
    public AuditoriaDtoResponse buscarId(Long id) {
        Auditoria a = auditoriaRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el registro de auditoría con id: " + id));
        return auditoriaMapper.entityToDto(a, usuarioMapper.entityToDto(a.getUsuario()));
    }

    @Override
    // Auditorías por usuario
    public List<AuditoriaDtoResponse> buscarUsuario(Long usuarioId) {
        return auditoriaRepository.findByUsuarioId(usuarioId).stream()
                .map(a -> auditoriaMapper.entityToDto(a, usuarioMapper.entityToDto(a.getUsuario())))
                .toList();
    }

    @Override
    // Auditorías por el tipo de operación
    public List<AuditoriaDtoResponse> buscarTipoOperacion(String tipoOperacion) {
        return auditoriaRepository.findByTipoOperacion(tipoOperacion).stream()
                .map(a -> auditoriaMapper.entityToDto(a, usuarioMapper.entityToDto(a.getUsuario())))
                .toList();
    }

    @Override
    public List<AuditoriaDtoResponse> buscarEntidadAfectada(String entidadAfectada) {
        return auditoriaRepository.findByEntidadAfectada(entidadAfectada).stream()
                .map(a -> auditoriaMapper.entityToDto(a, usuarioMapper.entityToDto(a.getUsuario())))
                .toList();
    }

    @Override
    public List<AuditoriaDtoResponse> buscarRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return auditoriaRepository.findByFechaBetween(fechaInicio, fechaFin).stream()
                .map(a -> auditoriaMapper.entityToDto(a, usuarioMapper.entityToDto(a.getUsuario())))
                .toList();
    }

    @Override
    public List<AuditoriaDtoResponse> buscarUsuarioYTipo(Long usuarioId, String tipoOperacion) {
        return auditoriaRepository.findByUsuarioIdAndTipoOperacion(usuarioId, tipoOperacion).stream()
                .map(a -> auditoriaMapper.entityToDto(a, usuarioMapper.entityToDto(a.getUsuario())))
                .toList();
    }
}
