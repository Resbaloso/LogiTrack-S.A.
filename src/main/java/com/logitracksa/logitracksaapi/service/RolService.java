package com.logitracksa.logitracksaapi.service;

import com.logitracksa.logitracksaapi.dto.request.RolDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.RolDtoResponse;

import java.util.List;

public interface RolService {

    RolDtoResponse guardar(RolDtoRequest dto);

    RolDtoResponse actualizar(RolDtoRequest dto, Long id);

    void eliminar(Long id);

    List<RolDtoResponse> listar();

    RolDtoResponse buscarId(Long id);

    List<RolDtoResponse> buscarNombre(String nombre);
}
