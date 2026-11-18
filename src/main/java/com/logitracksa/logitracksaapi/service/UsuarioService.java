package com.logitracksa.logitracksaapi.service;

import com.logitracksa.logitracksaapi.dto.request.UsuarioDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.UsuarioDtoResponse;

import java.util.List;

public interface UsuarioService {

    UsuarioDtoResponse guardar(UsuarioDtoRequest dto);

    UsuarioDtoResponse actualizar(UsuarioDtoRequest dto, Long id);

    void eliminar(Long id);

    List<UsuarioDtoResponse> listar();

    UsuarioDtoResponse buscarId(Long id);

    List<UsuarioDtoResponse> buscarEmail(String email);

    List<UsuarioDtoResponse> buscarActivos(boolean activo);
}
