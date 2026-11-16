package com.logitracksa.logitracksaapi.mapper;

import com.logitracksa.logitracksaapi.dto.request.DetalleMovimientoDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.DetalleMovimientoDtoResponse;
import com.logitracksa.logitracksaapi.dto.response.MovimientoDtoResponse;
import com.logitracksa.logitracksaapi.dto.response.ProductoDtoResponse;
import com.logitracksa.logitracksaapi.model.DetalleMovimiento;
import com.logitracksa.logitracksaapi.model.Movimiento;
import com.logitracksa.logitracksaapi.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class DetalleMovimientoMapper {

    public DetalleMovimientoDtoResponse entityToDto(DetalleMovimiento detalle,
                                                     MovimientoDtoResponse movimientoDto,
                                                     ProductoDtoResponse productoDto) {
        if (detalle == null || movimientoDto == null || productoDto == null) return null;
        return new DetalleMovimientoDtoResponse(
                detalle.getId(),
                movimientoDto,
                productoDto,
                detalle.getCantidad()
        );
    }

    public DetalleMovimiento dtoToEntity(DetalleMovimientoDtoRequest dto, Movimiento movimiento, Producto producto) {
        if (dto == null || movimiento == null || producto == null) return null;
        DetalleMovimiento detalle = new DetalleMovimiento();
        detalle.setMovimiento(movimiento);
        detalle.setProducto(producto);
        detalle.setCantidad(dto.cantidad());
        return detalle;
    }

    public void updateEntityFromDto(DetalleMovimientoDtoRequest dto, DetalleMovimiento detalle,
                                    Movimiento movimiento, Producto producto) {
        if (dto == null || detalle == null || movimiento == null || producto == null) return;
        detalle.setMovimiento(movimiento);
        detalle.setProducto(producto);
        detalle.setCantidad(dto.cantidad());
    }
}
