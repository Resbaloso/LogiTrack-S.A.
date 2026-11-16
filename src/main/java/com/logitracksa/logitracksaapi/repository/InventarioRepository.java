package com.logitracksa.logitracksaapi.repository;

import com.logitracksa.logitracksaapi.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventarioRepository extends JpaRepository<Inventario, Integer> {

    // Productos con stock < 10
    List<Inventario> findByStockLessThan(int stock);

    List<Inventario> findByBodegaId(Integer bodegaId);

    List<Inventario> findByProductoId(Integer productoId);

    List<Inventario> findByBodegaIdAndProductoId(Integer bodegaId, Integer productoId);

    boolean existsByBodegaIdAndProductoId(Integer bodegaId, Integer productoId);

    boolean existsByProductoId(Integer productoId);

    boolean existsByBodegaId(Integer bodegaId);
}
