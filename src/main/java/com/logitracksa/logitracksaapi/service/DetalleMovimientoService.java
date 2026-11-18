package com.logitracksa.logitracksaapi.service;

import com.logitracksa.logitracksaapi.dto.request.DetalleMovimientoDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.DetalleMovimientoDtoResponse;

import java.util.List;

public interface DetalleMovimientoService {

    DetalleMovimientoDtoResponse guardar(DetalleMovimientoDtoRequest dto);

    DetalleMovimientoDtoResponse actualizar(DetalleMovimientoDtoRequest dto, Long id);

    void eliminar(Long id);

    List<DetalleMovimientoDtoResponse> listar();

    DetalleMovimientoDtoResponse buscarId(Long id);

    List<DetalleMovimientoDtoResponse> buscarMovimiento(Long movimientoId);

    List<DetalleMovimientoDtoResponse> buscarProducto(Long productoId);

    List<DetalleMovimientoDtoResponse> buscarMovimientoYProducto(Long movimientoId, Long productoId);

    List<DetalleMovimientoDtoResponse> buscarCantidadMinima(int cantidad);
}
