package com.logitracksa.logitracksaapi.service.impl;

import com.logitracksa.logitracksaapi.dto.request.InventarioDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.BodegaDtoResponse;
import com.logitracksa.logitracksaapi.dto.response.InventarioDtoResponse;
import com.logitracksa.logitracksaapi.dto.response.ProductoDtoResponse;
import com.logitracksa.logitracksaapi.exception.BusinessRuleException;
import com.logitracksa.logitracksaapi.exception.DatabaseRuleException;
import com.logitracksa.logitracksaapi.mapper.BodegaMapper;
import com.logitracksa.logitracksaapi.mapper.InventarioMapper;
import com.logitracksa.logitracksaapi.mapper.ProductoMapper;
import com.logitracksa.logitracksaapi.mapper.UsuarioMapper;
import com.logitracksa.logitracksaapi.model.Bodega;
import com.logitracksa.logitracksaapi.model.Inventario;
import com.logitracksa.logitracksaapi.model.Producto;
import com.logitracksa.logitracksaapi.repository.BodegaRepository;
import com.logitracksa.logitracksaapi.repository.InventarioRepository;
import com.logitracksa.logitracksaapi.repository.ProductoRepository;
import com.logitracksa.logitracksaapi.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InventarioServiceImpl implements InventarioService {

    private final InventarioMapper inventarioMapper;
    private final InventarioRepository inventarioRepository;
    private final BodegaRepository bodegaRepository;
    private final BodegaMapper bodegaMapper;
    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    private final UsuarioMapper usuarioMapper;

    @Override
    public InventarioDtoResponse guardar(InventarioDtoRequest dto) {
        if (inventarioRepository.existsByBodegaIdAndProductoId(dto.bodegaId(), dto.productoId())) {
            throw new BusinessRuleException("Ya existe un registro de inventario para ese producto en esa bodega");
        }
        Bodega bodega = bodegaRepository.findById(dto.bodegaId())
                .orElseThrow(() -> new DatabaseRuleException("No existe la bodega con id: " + dto.bodegaId()));
        Producto producto = productoRepository.findById(dto.productoId())
                .orElseThrow(() -> new DatabaseRuleException("No existe el producto con id: " + dto.productoId()));
        Inventario inventario = inventarioMapper.dtoToEntity(dto, bodega, producto);
        BodegaDtoResponse bodegaDto = bodegaMapper.entityToDto(bodega, usuarioMapper.entityToDto(bodega.getUsuario()));
        ProductoDtoResponse productoDto = productoMapper.entityToDto(producto);
        return inventarioMapper.entityToDto(inventarioRepository.save(inventario), bodegaDto, productoDto);
    }

    @Override
    public InventarioDtoResponse actualizar(InventarioDtoRequest dto, Long id) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el inventario con id: " + id));
        Bodega bodega = bodegaRepository.findById(dto.bodegaId())
                .orElseThrow(() -> new DatabaseRuleException("No existe la bodega con id: " + dto.bodegaId()));
        Producto producto = productoRepository.findById(dto.productoId())
                .orElseThrow(() -> new DatabaseRuleException("No existe el producto con id: " + dto.productoId()));
        inventarioMapper.updateEntityFromDto(dto, inventario, bodega, producto);
        BodegaDtoResponse bodegaDto = bodegaMapper.entityToDto(bodega, usuarioMapper.entityToDto(bodega.getUsuario()));
        ProductoDtoResponse productoDto = productoMapper.entityToDto(producto);
        return inventarioMapper.entityToDto(inventarioRepository.save(inventario), bodegaDto, productoDto);
    }

    @Override
    public void eliminar(Long id) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el inventario con id: " + id));
        inventarioRepository.delete(inventario);
    }

    @Override
    public List<InventarioDtoResponse> listar() {
        return inventarioRepository.findAll().stream()
                .map(i -> inventarioMapper.entityToDto(i,
                        bodegaMapper.entityToDto(i.getBodega(), usuarioMapper.entityToDto(i.getBodega().getUsuario())),
                        productoMapper.entityToDto(i.getProducto())))
                .toList();
    }

    @Override
    public InventarioDtoResponse buscarId(Long id) {
        Inventario i = inventarioRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el inventario con id: " + id));
        return inventarioMapper.entityToDto(i,
                bodegaMapper.entityToDto(i.getBodega(), usuarioMapper.entityToDto(i.getBodega().getUsuario())),
                productoMapper.entityToDto(i.getProducto()));
    }

    @Override
    public List<InventarioDtoResponse> buscarBodega(Long bodegaId) {
        return inventarioRepository.findByBodegaId(bodegaId).stream()
                .map(i -> inventarioMapper.entityToDto(i,
                        bodegaMapper.entityToDto(i.getBodega(), usuarioMapper.entityToDto(i.getBodega().getUsuario())),
                        productoMapper.entityToDto(i.getProducto())))
                .toList();
    }

    @Override
    public List<InventarioDtoResponse> buscarProducto(Long productoId) {
        return inventarioRepository.findByProductoId(productoId).stream()
                .map(i -> inventarioMapper.entityToDto(i,
                        bodegaMapper.entityToDto(i.getBodega(), usuarioMapper.entityToDto(i.getBodega().getUsuario())),
                        productoMapper.entityToDto(i.getProducto())))
                .toList();
    }

    @Override
    public List<InventarioDtoResponse> buscarBodegaYProducto(Long bodegaId, Long productoId) {
        return inventarioRepository.findByBodegaIdAndProductoId(bodegaId, productoId).stream()
                .map(i -> inventarioMapper.entityToDto(i,
                        bodegaMapper.entityToDto(i.getBodega(), usuarioMapper.entityToDto(i.getBodega().getUsuario())),
                        productoMapper.entityToDto(i.getProducto())))
                .toList();
    }

    @Override
    // Productos con stock menor a 10
    public List<InventarioDtoResponse> buscarStockBajo(int umbral) {
        return inventarioRepository.findByStockLessThan(umbral).stream()
                .map(i -> inventarioMapper.entityToDto(i,
                        bodegaMapper.entityToDto(i.getBodega(), usuarioMapper.entityToDto(i.getBodega().getUsuario())),
                        productoMapper.entityToDto(i.getProducto())))
                .toList();
    }
}
