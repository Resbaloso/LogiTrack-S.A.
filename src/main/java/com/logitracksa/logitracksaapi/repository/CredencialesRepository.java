package com.logitracksa.logitracksaapi.repository;

import com.logitracksa.logitracksaapi.model.Credenciales;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CredencialesRepository extends JpaRepository<Credenciales, Integer> {

    List<Credenciales> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUsuarioId(Integer usuarioId);

    List<Credenciales> findByRolId(Integer rolId);

    List<Credenciales> findByUsuarioId(Integer usuarioId);
}
