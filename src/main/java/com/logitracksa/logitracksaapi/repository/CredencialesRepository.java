package com.logitracksa.logitracksaapi.repository;

import com.logitracksa.logitracksaapi.model.Credenciales;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CredencialesRepository extends JpaRepository<Credenciales, Long> {

    List<Credenciales> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUsuarioId(Long usuarioId);

    List<Credenciales> findByRolId(Long rolId);

    List<Credenciales> findByUsuarioId(Long usuarioId);
}
