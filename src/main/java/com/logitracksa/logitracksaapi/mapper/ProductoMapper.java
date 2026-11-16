package com.logitracksa.logitracksaapi.mapper;

import com.logitracksa.logitracksaapi.dto.request.ProductoDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.ProductoDtoResponse;
import com.logitracksa.logitracksaapi.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public ProductoDtoResponse entityToDto(Producto producto) {
        if (producto == null) return null;
        return new ProductoDtoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getCategoria(),
                producto.getPrecio()
        );
    }

    public Producto dtoToEntity(ProductoDtoRequest dto) {
        if (dto == null) return null;
        Producto producto = new Producto();
        producto.setNombre(dto.nombre());
        producto.setDescripcion(dto.descripcion());
        producto.setCategoria(dto.categoria());
        producto.setPrecio(dto.precio());
        return producto;
    }

    public void updateEntityFromDto(ProductoDtoRequest dto, Producto producto) {
        if (dto == null || producto == null) return;
        producto.setNombre(dto.nombre());
        producto.setDescripcion(dto.descripcion());
        producto.setCategoria(dto.categoria());
        producto.setPrecio(dto.precio());
    }
}
