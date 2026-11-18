package com.logitracksa.logitracksaapi.service.impl;

import com.logitracksa.logitracksaapi.dto.request.ProductoDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.ProductoDtoResponse;
import com.logitracksa.logitracksaapi.exception.BusinessRuleException;
import com.logitracksa.logitracksaapi.exception.DatabaseRuleException;
import com.logitracksa.logitracksaapi.mapper.ProductoMapper;
import com.logitracksa.logitracksaapi.model.Producto;
import com.logitracksa.logitracksaapi.repository.DetalleMovimientoRepository;
import com.logitracksa.logitracksaapi.repository.InventarioRepository;
import com.logitracksa.logitracksaapi.repository.ProductoRepository;
import com.logitracksa.logitracksaapi.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoMapper productoMapper;
    private final ProductoRepository productoRepository;
    private final InventarioRepository inventarioRepository;
    private final DetalleMovimientoRepository detalleMovimientoRepository;

    @Override
    public ProductoDtoResponse guardar(ProductoDtoRequest dto) {
        if (dto.precio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("El precio del producto debe ser mayor a cero");
        }
        Producto producto = productoMapper.dtoToEntity(dto);
        return productoMapper.entityToDto(productoRepository.save(producto));
    }

    @Override
    public ProductoDtoResponse actualizar(ProductoDtoRequest dto, Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el producto con id: " + id));
        productoMapper.updateEntityFromDto(dto, producto);
        return productoMapper.entityToDto(productoRepository.save(producto));
    }

    @Override
    public void eliminar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el producto con id: " + id));
        if (inventarioRepository.existsByProductoId(producto.getId())) {
            throw new BusinessRuleException("No se puede eliminar el producto porque tiene inventario registrado");
        }
        if (detalleMovimientoRepository.existsByProductoId(producto.getId())) {
            throw new BusinessRuleException("No se puede eliminar el producto porque está asociado a movimientos");
        }
        productoRepository.delete(producto);
    }

    @Override
    public List<ProductoDtoResponse> listar() {
        return productoRepository.findAll().stream()
                .map(productoMapper::entityToDto)
                .toList();
    }

    @Override
    public ProductoDtoResponse buscarId(Long id) {
        return productoMapper.entityToDto(productoRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el producto con id: " + id)));
    }

    @Override
    public List<ProductoDtoResponse> buscarCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria).stream()
                .map(productoMapper::entityToDto)
                .toList();
    }

    @Override
    public List<ProductoDtoResponse> buscarNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(productoMapper::entityToDto)
                .toList();
    }

    @Override
    public List<ProductoDtoResponse> buscarPrecioMaximo(BigDecimal precio) {
        return productoRepository.findByPrecioLessThanEqual(precio).stream()
                .map(productoMapper::entityToDto)
                .toList();
    }

    @Override
    public List<ProductoDtoResponse> buscarPrecioMinimo(BigDecimal precio) {
        return productoRepository.findByPrecioGreaterThanEqual(precio).stream()
                .map(productoMapper::entityToDto)
                .toList();
    }
}
