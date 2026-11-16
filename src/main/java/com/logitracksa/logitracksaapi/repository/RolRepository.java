package com.logitracksa.logitracksaapi.repository;

import com.logitracksa.logitracksaapi.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolRepository extends JpaRepository<Rol, Integer> {

    List<Rol> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
