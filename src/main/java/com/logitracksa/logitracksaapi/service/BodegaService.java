package com.logitracksa.logitracksaapi.service;

import com.logitracksa.logitracksaapi.dto.request.BodegaDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.BodegaDtoResponse;

import java.util.List;

public interface BodegaService {

    BodegaDtoResponse guardar(BodegaDtoRequest dto);

    BodegaDtoResponse actualizar(BodegaDtoRequest dto, Integer id);

    void eliminar(Integer id);

    List<BodegaDtoResponse> listar();

    BodegaDtoResponse buscarId(Integer id);

    List<BodegaDtoResponse> buscarNombre(String nombre);

    List<BodegaDtoResponse> buscarUbicacion(String ubicacion);

    List<BodegaDtoResponse> buscarEncargado(Integer usuarioId);

    List<BodegaDtoResponse> buscarCapacidadMinima(int capacidad);
}
