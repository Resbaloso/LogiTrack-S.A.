package com.logitracksa.logitracksaapi.service;

import com.logitracksa.logitracksaapi.dto.request.ProductoDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.ProductoDtoResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoService {

    ProductoDtoResponse guardar(ProductoDtoRequest dto);

    ProductoDtoResponse actualizar(ProductoDtoRequest dto, Integer id);

    void eliminar(Integer id);

    List<ProductoDtoResponse> listar();

    ProductoDtoResponse buscarId(Integer id);

    List<ProductoDtoResponse> buscarCategoria(String categoria);

    List<ProductoDtoResponse> buscarNombre(String nombre);

    List<ProductoDtoResponse> buscarPrecioMaximo(BigDecimal precio);

    List<ProductoDtoResponse> buscarPrecioMinimo(BigDecimal precio);
}
