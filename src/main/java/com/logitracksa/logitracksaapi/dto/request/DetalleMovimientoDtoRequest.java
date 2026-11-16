package com.logitracksa.logitracksaapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DetalleMovimientoDtoRequest(
        @Schema(description = "ID del movimiento al que pertenece este detalle", example = "1")
        @NotNull(message = "El ID del movimiento no puede estar nulo")
        @Positive(message = "El ID del movimiento debe ser positivo")
        Integer movimientoId,

        @Schema(description = "ID del producto incluido en el movimiento", example = "1")
        @NotNull(message = "El ID del producto no puede estar nulo")
        @Positive(message = "El ID del producto debe ser positivo")
        Integer productoId,

        @Schema(description = "Cantidad de unidades del producto en el movimiento", example = "50")
        @NotNull(message = "La cantidad no puede estar nula")
        @Positive(message = "La cantidad debe ser un valor positivo")
        Integer cantidad
) {}
