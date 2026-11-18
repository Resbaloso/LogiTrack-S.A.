package com.logitracksa.logitracksaapi.repository;

import com.logitracksa.logitracksaapi.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByUsuarioId(Long usuarioId);

    List<Movimiento> findByTipoMovimiento(String tipoMovimiento);

    // Moovimientos entre un rango de fechas
    List<Movimiento> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<Movimiento> findByBodegaOrigenId(Long bodegaOrigenId);

    List<Movimiento> findByBodegaDestinoId(Long bodegaDestinoId);

    List<Movimiento> findByUsuarioIdAndTipoMovimiento(Long usuarioId, String tipoMovimiento);

    boolean existsByUsuarioId(Long usuarioId);

    boolean existsByBodegaOrigenId(Long bodegaOrigenId);

    boolean existsByBodegaDestinoId(Long bodegaDestinoId);
}
