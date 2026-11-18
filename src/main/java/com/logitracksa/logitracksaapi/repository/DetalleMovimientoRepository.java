package com.logitracksa.logitracksaapi.repository;

import com.logitracksa.logitracksaapi.model.DetalleMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleMovimientoRepository extends JpaRepository<DetalleMovimiento, Long> {

    List<DetalleMovimiento> findByMovimientoId(Long movimientoId);

    List<DetalleMovimiento> findByProductoId(Long productoId);

    boolean existsByMovimientoId(Long movimientoId);

    boolean existsByProductoId(Long productoId);

    List<DetalleMovimiento> findByMovimientoIdAndProductoId(Long movimientoId, Long productoId);

    // Para el reporte de los más movidos
    List<DetalleMovimiento> findByCantidadGreaterThanEqual(int cantidad);
}
