package com.logitracksa.logitracksaapi.repository;

import com.logitracksa.logitracksaapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    List<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Usuario> findByActivo(boolean activo);
}
