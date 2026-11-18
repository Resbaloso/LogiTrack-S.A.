package com.logitracksa.logitracksaapi.service;

import com.logitracksa.logitracksaapi.dto.request.CredencialesDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.CredencialesDtoResponse;

import java.util.List;

public interface CredencialesService {

    CredencialesDtoResponse guardar(CredencialesDtoRequest dto);

    CredencialesDtoResponse actualizar(CredencialesDtoRequest dto, Long id);

    void eliminar(Long id);

    List<CredencialesDtoResponse> listar();

    CredencialesDtoResponse buscarId(Long id);

    List<CredencialesDtoResponse> buscarUsername(String username);

    List<CredencialesDtoResponse> buscarUsuarioId(Long usuarioId);

    List<CredencialesDtoResponse> buscarRolId(Long rolId);
}
