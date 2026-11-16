package com.logitracksa.logitracksaapi.dto.response;

import java.time.LocalDate;

public record MovimientoDtoResponse(
        Long id,
        LocalDate fecha,
        String tipoMovimiento,
        UsuarioDtoResponse usuario,
        BodegaDtoResponse bodegaOrigen,
        BodegaDtoResponse bodegaDestino
) {}
