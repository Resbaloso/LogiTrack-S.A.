package com.logitracksa.logitracksaapi.service;

import com.logitracksa.logitracksaapi.dto.request.AuditoriaDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.AuditoriaDtoResponse;

import java.time.LocalDate;
import java.util.List;

public interface AuditoriaService {

    AuditoriaDtoResponse guardar(AuditoriaDtoRequest dto);

    AuditoriaDtoResponse actualizar(AuditoriaDtoRequest dto, Integer id);

    void eliminar(Integer id);

    List<AuditoriaDtoResponse> listar();

    AuditoriaDtoResponse buscarId(Integer id);

    // Auditorías por usuario
    List<AuditoriaDtoResponse> buscarUsuario(Integer usuarioId);

    // Auditorías por tipo de operación
    List<AuditoriaDtoResponse> buscarTipoOperacion(String tipoOperacion);

    List<AuditoriaDtoResponse> buscarEntidadAfectada(String entidadAfectada);

    List<AuditoriaDtoResponse> buscarRangoFechas(LocalDate fechaInicio, LocalDate fechaFin);

    List<AuditoriaDtoResponse> buscarUsuarioYTipo(Integer usuarioId, String tipoOperacion);
}
