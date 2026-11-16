package com.logitracksa.logitracksaapi.service;

import com.logitracksa.logitracksaapi.dto.request.UsuarioDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.UsuarioDtoResponse;

import java.util.List;

public interface UsuarioService {

    UsuarioDtoResponse guardar(UsuarioDtoRequest dto);

    UsuarioDtoResponse actualizar(UsuarioDtoRequest dto, Integer id);

    void eliminar(Integer id);

    List<UsuarioDtoResponse> listar();

    UsuarioDtoResponse buscarId(Integer id);

    UsuarioDtoResponse buscarEmail(String email);

    List<UsuarioDtoResponse> buscarActivos(boolean activo);
}
