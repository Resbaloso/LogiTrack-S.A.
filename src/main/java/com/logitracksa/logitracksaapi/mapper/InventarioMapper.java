package com.logitracksa.logitracksaapi.mapper;

import com.logitracksa.logitracksaapi.dto.request.InventarioDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.BodegaDtoResponse;
import com.logitracksa.logitracksaapi.dto.response.InventarioDtoResponse;
import com.logitracksa.logitracksaapi.dto.response.ProductoDtoResponse;
import com.logitracksa.logitracksaapi.model.Bodega;
import com.logitracksa.logitracksaapi.model.Inventario;
import com.logitracksa.logitracksaapi.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class InventarioMapper {

    public InventarioDtoResponse entityToDto(Inventario inventario, BodegaDtoResponse bodegaDto, ProductoDtoResponse productoDto) {
        if (inventario == null || bodegaDto == null || productoDto == null) return null;
        return new InventarioDtoResponse(
                inventario.getId(),
                bodegaDto,
                productoDto,
                inventario.getStock()
        );
    }

    public Inventario dtoToEntity(InventarioDtoRequest dto, Bodega bodega, Producto producto) {
        if (dto == null || bodega == null || producto == null) return null;
        Inventario inventario = new Inventario();
        inventario.setBodega(bodega);
        inventario.setProducto(producto);
        inventario.setStock(dto.stock());
        return inventario;
    }

    public void updateEntityFromDto(InventarioDtoRequest dto, Inventario inventario, Bodega bodega, Producto producto) {
        if (dto == null || inventario == null || bodega == null || producto == null) return;
        inventario.setBodega(bodega);
        inventario.setProducto(producto);
        inventario.setStock(dto.stock());
    }
}
