package com.logitracksa.logitracksaapi.repository;

import com.logitracksa.logitracksaapi.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {

    List<Movimiento> findByUsuarioId(Integer usuarioId);

    List<Movimiento> findByTipoMovimiento(String tipoMovimiento);

    // Movimientos por rango de fechas
    List<Movimiento> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<Movimiento> findByBodegaOrigenId(Integer bodegaOrigenId);

    List<Movimiento> findByBodegaDestinoId(Integer bodegaDestinoId);

    List<Movimiento> findByUsuarioIdAndTipoMovimiento(Integer usuarioId, String tipoMovimiento);

    boolean existsByUsuarioId(Integer usuarioId);

    boolean existsByBodegaOrigenId(Integer bodegaOrigenId);

    boolean existsByBodegaDestinoId(Integer bodegaDestinoId);
}
