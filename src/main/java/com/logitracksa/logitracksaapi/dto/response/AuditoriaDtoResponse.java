package com.logitracksa.logitracksaapi.dto.response;

import java.time.LocalDate;

public record AuditoriaDtoResponse(
        Long id,
        String tipoOperacion,
        LocalDate fecha,
        UsuarioDtoResponse usuario,
        String entidadAfectada,
        String valoresAnteriores,
        String valoresNuevos
) {}
