package com.logitracksa.logitracksaapi.dto.response;

import java.math.BigDecimal;

public record ProductoDtoResponse(
        Long id,
        String nombre,
        String descripcion,
        String categoria,
        BigDecimal precio
) {}
