package com.logitracksa.logitracksaapi.service;

import com.logitracksa.logitracksaapi.dto.request.DetalleMovimientoDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.DetalleMovimientoDtoResponse;

import java.util.List;

public interface DetalleMovimientoService {

    DetalleMovimientoDtoResponse guardar(DetalleMovimientoDtoRequest dto);

    DetalleMovimientoDtoResponse actualizar(DetalleMovimientoDtoRequest dto, Integer id);

    void eliminar(Integer id);

    List<DetalleMovimientoDtoResponse> listar();

    DetalleMovimientoDtoResponse buscarId(Integer id);

    List<DetalleMovimientoDtoResponse> buscarMovimiento(Integer movimientoId);

    List<DetalleMovimientoDtoResponse> buscarProducto(Integer productoId);

    List<DetalleMovimientoDtoResponse> buscarMovimientoYProducto(Integer movimientoId, Integer productoId);

    List<DetalleMovimientoDtoResponse> buscarCantidadMinima(int cantidad);
}
