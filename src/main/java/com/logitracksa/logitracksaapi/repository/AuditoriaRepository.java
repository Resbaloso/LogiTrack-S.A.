package com.logitracksa.logitracksaapi.repository;

import com.logitracksa.logitracksaapi.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {

    // Auditorías por usuario
    List<Auditoria> findByUsuarioId(Long usuarioId);

    // Auditorías por tipo de operación
    List<Auditoria> findByTipoOperacion(String tipoOperacion);

    List<Auditoria> findByEntidadAfectada(String entidadAfectada);

    List<Auditoria> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<Auditoria> findByUsuarioIdAndTipoOperacion(Long usuarioId, String tipoOperacion);
}
