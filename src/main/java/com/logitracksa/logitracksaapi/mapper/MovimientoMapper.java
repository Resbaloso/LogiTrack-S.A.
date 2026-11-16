package com.logitracksa.logitracksaapi.mapper;

import com.logitracksa.logitracksaapi.dto.request.MovimientoDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.BodegaDtoResponse;
import com.logitracksa.logitracksaapi.dto.response.MovimientoDtoResponse;
import com.logitracksa.logitracksaapi.dto.response.UsuarioDtoResponse;
import com.logitracksa.logitracksaapi.model.Bodega;
import com.logitracksa.logitracksaapi.model.Movimiento;
import com.logitracksa.logitracksaapi.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class MovimientoMapper {

    public MovimientoDtoResponse entityToDto(Movimiento movimiento, UsuarioDtoResponse usuarioDto,
                                              BodegaDtoResponse bodegaOrigenDto, BodegaDtoResponse bodegaDestinoDto) {
        if (movimiento == null || usuarioDto == null || bodegaOrigenDto == null || bodegaDestinoDto == null) return null;
        return new MovimientoDtoResponse(
                movimiento.getId(),
                movimiento.getFecha(),
                movimiento.getTipoMovimiento(),
                usuarioDto,
                bodegaOrigenDto,
                bodegaDestinoDto
        );
    }

    public Movimiento dtoToEntity(MovimientoDtoRequest dto, Usuario usuario, Bodega bodegaOrigen, Bodega bodegaDestino) {
        if (dto == null || usuario == null || bodegaOrigen == null || bodegaDestino == null) return null;
        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(dto.fecha());
        movimiento.setTipoMovimiento(dto.tipoMovimiento());
        movimiento.setUsuario(usuario);
        movimiento.setBodegaOrigen(bodegaOrigen);
        movimiento.setBodegaDestino(bodegaDestino);
        return movimiento;
    }

    public void updateEntityFromDto(MovimientoDtoRequest dto, Movimiento movimiento,
                                    Usuario usuario, Bodega bodegaOrigen, Bodega bodegaDestino) {
        if (dto == null || movimiento == null || usuario == null || bodegaOrigen == null || bodegaDestino == null) return;
        movimiento.setFecha(dto.fecha());
        movimiento.setTipoMovimiento(dto.tipoMovimiento());
        movimiento.setUsuario(usuario);
        movimiento.setBodegaOrigen(bodegaOrigen);
        movimiento.setBodegaDestino(bodegaDestino);
    }
}
