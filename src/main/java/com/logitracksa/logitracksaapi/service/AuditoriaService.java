package com.logitracksa.logitracksaapi.service;

import com.logitracksa.logitracksaapi.dto.request.AuditoriaDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.AuditoriaDtoResponse;

import java.time.LocalDate;
import java.util.List;

public interface AuditoriaService {

    AuditoriaDtoResponse guardar(AuditoriaDtoRequest dto);

    AuditoriaDtoResponse actualizar(AuditoriaDtoRequest dto, Long id);

    void eliminar(Long id);

    List<AuditoriaDtoResponse> listar();

    AuditoriaDtoResponse buscarId(Long id);

    // Auditorías por usuario
    List<AuditoriaDtoResponse> buscarUsuario(Long usuarioId);

    // Auditorías por tipo de operación
    List<AuditoriaDtoResponse> buscarTipoOperacion(String tipoOperacion);

    List<AuditoriaDtoResponse> buscarEntidadAfectada(String entidadAfectada);

    List<AuditoriaDtoResponse> buscarRangoFechas(LocalDate fechaInicio, LocalDate fechaFin);

    List<AuditoriaDtoResponse> buscarUsuarioYTipo(Long usuarioId, String tipoOperacion);
}
