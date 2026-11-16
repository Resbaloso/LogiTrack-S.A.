package com.logitracksa.logitracksaapi.service;

import com.logitracksa.logitracksaapi.dto.request.RolDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.RolDtoResponse;

import java.util.List;

public interface RolService {

    RolDtoResponse guardar(RolDtoRequest dto);

    RolDtoResponse actualizar(RolDtoRequest dto, Integer id);

    void eliminar(Integer id);

    List<RolDtoResponse> listar();

    RolDtoResponse buscarId(Integer id);

    List<RolDtoResponse> buscarNombre(String nombre);
}
