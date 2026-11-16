package com.logitracksa.logitracksaapi.dto.response;

public record DetalleMovimientoDtoResponse(
        Long id,
        MovimientoDtoResponse movimiento,
        ProductoDtoResponse producto,
        Integer cantidad
) {}
