package com.logitracksa.logitracksaapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record MovimientoDtoRequest(
        @Schema(description = "Fecha en que se realiza el movimiento", example = "2026-11-15")
        @NotNull(message = "La fecha no puede estar nula")
        LocalDate fecha,

        @Schema(description = "Tipo de movimiento: ENTRADA, SALIDA o TRANSFERENCIA", example = "ENTRADA")
        @NotBlank(message = "El tipo de movimiento no puede estar vacío")
        String tipoMovimiento,

        @Schema(description = "ID del usuario responsable del movimiento", example = "1")
        @NotNull(message = "El ID del usuario no puede estar nulo")
        @Positive(message = "El ID del usuario debe ser positivo")
        Long usuarioId,

        @Schema(description = "ID de la bodega de origen del movimiento", example = "1")
        @NotNull(message = "El ID de la bodega origen no puede estar nulo")
        @Positive(message = "El ID de la bodega origen debe ser positivo")
        Long bodegaOrigenId,

        @Schema(description = "ID de la bodega de destino del movimiento", example = "2")
        @NotNull(message = "El ID de la bodega destino no puede estar nulo")
        @Positive(message = "El ID de la bodega destino debe ser positivo")
        Long bodegaDestinoId
) {}
