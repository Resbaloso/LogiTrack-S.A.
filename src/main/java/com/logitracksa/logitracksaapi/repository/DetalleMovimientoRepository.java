package com.logitracksa.logitracksaapi.repository;

import com.logitracksa.logitracksaapi.model.DetalleMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleMovimientoRepository extends JpaRepository<DetalleMovimiento, Integer> {

    List<DetalleMovimiento> findByMovimientoId(Integer movimientoId);

    List<DetalleMovimiento> findByProductoId(Integer productoId);

    boolean existsByMovimientoId(Integer movimientoId);

    boolean existsByProductoId(Integer productoId);

    List<DetalleMovimiento> findByMovimientoIdAndProductoId(Integer movimientoId, Integer productoId);

    // Para el reporte de producto con más movimientos
    List<DetalleMovimiento> findByCantidadGreaterThanEqual(int cantidad);
}
