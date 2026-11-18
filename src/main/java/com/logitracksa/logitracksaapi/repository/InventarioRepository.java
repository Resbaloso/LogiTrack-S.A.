package com.logitracksa.logitracksaapi.repository;

import com.logitracksa.logitracksaapi.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    // Productos con stock menor a 10
    List<Inventario> findByStockLessThan(int stock);

    List<Inventario> findByBodegaId(Long bodegaId);

    List<Inventario> findByProductoId(Long productoId);

    List<Inventario> findByBodegaIdAndProductoId(Long bodegaId, Long productoId);

    boolean existsByBodegaIdAndProductoId(Long bodegaId, Long productoId);

    boolean existsByProductoId(Long productoId);

    boolean existsByBodegaId(Long bodegaId);
}
