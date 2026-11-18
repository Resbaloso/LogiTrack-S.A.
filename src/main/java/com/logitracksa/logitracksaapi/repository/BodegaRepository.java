package com.logitracksa.logitracksaapi.repository;

import com.logitracksa.logitracksaapi.model.Bodega;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BodegaRepository extends JpaRepository<Bodega, Long> {

    List<Bodega> findByNombreContainingIgnoreCase(String nombre);

    List<Bodega> findByUbicacionContainingIgnoreCase(String ubicacion);

    List<Bodega> findByUsuarioId(Long usuarioId);

    boolean existsByUsuarioId(Long usuarioId);

    List<Bodega> findByCapacidadGreaterThanEqual(int capacidad);
}
