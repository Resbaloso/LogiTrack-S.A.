package com.logitracksa.logitracksaapi.service;

import com.logitracksa.logitracksaapi.dto.request.CredencialesDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.CredencialesDtoResponse;

import java.util.List;

public interface CredencialesService {

    CredencialesDtoResponse guardar(CredencialesDtoRequest dto);

    CredencialesDtoResponse actualizar(CredencialesDtoRequest dto, Integer id);

    void eliminar(Integer id);

    List<CredencialesDtoResponse> listar();

    CredencialesDtoResponse buscarId(Integer id);

    List<CredencialesDtoResponse> buscarUsername(String username);

    List<CredencialesDtoResponse> buscarUsuarioId(Integer usuarioId);

    List<CredencialesDtoResponse> buscarRolId(Integer rolId);
}
