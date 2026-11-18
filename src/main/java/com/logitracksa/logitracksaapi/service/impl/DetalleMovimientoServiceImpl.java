package com.logitracksa.logitracksaapi.service.impl;

import com.logitracksa.logitracksaapi.dto.request.DetalleMovimientoDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.DetalleMovimientoDtoResponse;
import com.logitracksa.logitracksaapi.dto.response.MovimientoDtoResponse;
import com.logitracksa.logitracksaapi.dto.response.ProductoDtoResponse;
import com.logitracksa.logitracksaapi.exception.BusinessRuleException;
import com.logitracksa.logitracksaapi.exception.DatabaseRuleException;
import com.logitracksa.logitracksaapi.mapper.BodegaMapper;
import com.logitracksa.logitracksaapi.mapper.DetalleMovimientoMapper;
import com.logitracksa.logitracksaapi.mapper.MovimientoMapper;
import com.logitracksa.logitracksaapi.mapper.ProductoMapper;
import com.logitracksa.logitracksaapi.mapper.UsuarioMapper;
import com.logitracksa.logitracksaapi.model.DetalleMovimiento;
import com.logitracksa.logitracksaapi.model.Movimiento;
import com.logitracksa.logitracksaapi.model.Producto;
import com.logitracksa.logitracksaapi.repository.DetalleMovimientoRepository;
import com.logitracksa.logitracksaapi.repository.MovimientoRepository;
import com.logitracksa.logitracksaapi.repository.ProductoRepository;
import com.logitracksa.logitracksaapi.service.DetalleMovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DetalleMovimientoServiceImpl implements DetalleMovimientoService {

    private final DetalleMovimientoMapper detalleMovimientoMapper;
    private final DetalleMovimientoRepository detalleMovimientoRepository;
    private final MovimientoRepository movimientoRepository;
    private final MovimientoMapper movimientoMapper;
    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    private final BodegaMapper bodegaMapper;
    private final UsuarioMapper usuarioMapper;

    @Override
    public DetalleMovimientoDtoResponse guardar(DetalleMovimientoDtoRequest dto) {
        if (dto.cantidad() <= 0) {
            throw new BusinessRuleException("La cantidad debe ser mayor a cero");
        }
        Movimiento movimiento = movimientoRepository.findById(dto.movimientoId())
                .orElseThrow(() -> new DatabaseRuleException("No existe el movimiento con id: " + dto.movimientoId()));
        Producto producto = productoRepository.findById(dto.productoId())
                .orElseThrow(() -> new DatabaseRuleException("No existe el producto con id: " + dto.productoId()));
        DetalleMovimiento detalle = detalleMovimientoMapper.dtoToEntity(dto, movimiento, producto);
        MovimientoDtoResponse movimientoDto = buildMovimientoDto(movimiento);
        ProductoDtoResponse productoDto = productoMapper.entityToDto(producto);
        return detalleMovimientoMapper.entityToDto(detalleMovimientoRepository.save(detalle), movimientoDto, productoDto);
    }

    @Override
    public DetalleMovimientoDtoResponse actualizar(DetalleMovimientoDtoRequest dto, Long id) {
        DetalleMovimiento detalle = detalleMovimientoRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el detalle de movimiento con id: " + id));
        Movimiento movimiento = movimientoRepository.findById(dto.movimientoId())
                .orElseThrow(() -> new DatabaseRuleException("No existe el movimiento con id: " + dto.movimientoId()));
        Producto producto = productoRepository.findById(dto.productoId())
                .orElseThrow(() -> new DatabaseRuleException("No existe el producto con id: " + dto.productoId()));
        detalleMovimientoMapper.updateEntityFromDto(dto, detalle, movimiento, producto);
        MovimientoDtoResponse movimientoDto = buildMovimientoDto(movimiento);
        ProductoDtoResponse productoDto = productoMapper.entityToDto(producto);
        return detalleMovimientoMapper.entityToDto(detalleMovimientoRepository.save(detalle), movimientoDto, productoDto);
    }

    @Override
    public void eliminar(Long id) {
        DetalleMovimiento detalle = detalleMovimientoRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el detalle de movimiento con id: " + id));
        detalleMovimientoRepository.delete(detalle);
    }

    @Override
    public List<DetalleMovimientoDtoResponse> listar() {
        return detalleMovimientoRepository.findAll().stream()
                .map(d -> detalleMovimientoMapper.entityToDto(d,
                        buildMovimientoDto(d.getMovimiento()),
                        productoMapper.entityToDto(d.getProducto())))
                .toList();
    }

    @Override
    public DetalleMovimientoDtoResponse buscarId(Long id) {
        DetalleMovimiento d = detalleMovimientoRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el detalle de movimiento con id: " + id));
        return detalleMovimientoMapper.entityToDto(d,
                buildMovimientoDto(d.getMovimiento()),
                productoMapper.entityToDto(d.getProducto()));
    }

    @Override
    public List<DetalleMovimientoDtoResponse> buscarMovimiento(Long movimientoId) {
        return detalleMovimientoRepository.findByMovimientoId(movimientoId).stream()
                .map(d -> detalleMovimientoMapper.entityToDto(d,
                        buildMovimientoDto(d.getMovimiento()),
                        productoMapper.entityToDto(d.getProducto())))
                .toList();
    }

    @Override
    public List<DetalleMovimientoDtoResponse> buscarProducto(Long productoId) {
        return detalleMovimientoRepository.findByProductoId(productoId).stream()
                .map(d -> detalleMovimientoMapper.entityToDto(d,
                        buildMovimientoDto(d.getMovimiento()),
                        productoMapper.entityToDto(d.getProducto())))
                .toList();
    }

    @Override
    public List<DetalleMovimientoDtoResponse> buscarMovimientoYProducto(Long movimientoId, Long productoId) {
        return detalleMovimientoRepository.findByMovimientoIdAndProductoId(movimientoId, productoId).stream()
                .map(d -> detalleMovimientoMapper.entityToDto(d,
                        buildMovimientoDto(d.getMovimiento()),
                        productoMapper.entityToDto(d.getProducto())))
                .toList();
    }

    @Override
    public List<DetalleMovimientoDtoResponse> buscarCantidadMinima(int cantidad) {
        return detalleMovimientoRepository.findByCantidadGreaterThanEqual(cantidad).stream()
                .map(d -> detalleMovimientoMapper.entityToDto(d,
                        buildMovimientoDto(d.getMovimiento()),
                        productoMapper.entityToDto(d.getProducto())))
                .toList();
    }

    // Es un metodo auxiliar que permite acceder a los atributos anidados a movimiento sin la necesidad de duplicar el código.
    private MovimientoDtoResponse buildMovimientoDto(Movimiento m) {
        return movimientoMapper.entityToDto(m,
                usuarioMapper.entityToDto(m.getUsuario()),
                bodegaMapper.entityToDto(m.getBodegaOrigen(), usuarioMapper.entityToDto(m.getBodegaOrigen().getUsuario())),
                bodegaMapper.entityToDto(m.getBodegaDestino(), usuarioMapper.entityToDto(m.getBodegaDestino().getUsuario())));
    }
}
