package com.logitracksa.logitracksaapi.service;

import com.logitracksa.logitracksaapi.dto.request.InventarioDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.InventarioDtoResponse;

import java.util.List;

public interface InventarioService {

    InventarioDtoResponse guardar(InventarioDtoRequest dto);

    InventarioDtoResponse actualizar(InventarioDtoRequest dto, Integer id);

    void eliminar(Integer id);

    List<InventarioDtoResponse> listar();

    InventarioDtoResponse buscarId(Integer id);

    List<InventarioDtoResponse> buscarBodega(Integer bodegaId);

    List<InventarioDtoResponse> buscarProducto(Integer productoId);

    List<InventarioDtoResponse> buscarBodegaYProducto(Integer bodegaId, Integer productoId);

    // productos con stock < 10
    List<InventarioDtoResponse> buscarStockBajo(int umbral);
}
