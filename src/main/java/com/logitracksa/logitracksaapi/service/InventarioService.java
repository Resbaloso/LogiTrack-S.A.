package com.logitracksa.logitracksaapi.service;

import com.logitracksa.logitracksaapi.dto.request.InventarioDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.InventarioDtoResponse;

import java.util.List;

public interface InventarioService {

    InventarioDtoResponse guardar(InventarioDtoRequest dto);

    InventarioDtoResponse actualizar(InventarioDtoRequest dto, Long id);

    void eliminar(Long id);

    List<InventarioDtoResponse> listar();

    InventarioDtoResponse buscarId(Long id);

    List<InventarioDtoResponse> buscarBodega(Long bodegaId);

    List<InventarioDtoResponse> buscarProducto(Long productoId);

    List<InventarioDtoResponse> buscarBodegaYProducto(Long bodegaId, Long productoId);

    // Productos con stock bajo (< 10)
    List<InventarioDtoResponse> buscarStockBajo(int umbral);
}
