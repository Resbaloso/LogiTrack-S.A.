package com.logitracksa.logitracksaapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Positive;

public record InventarioDtoRequest(
        @Schema(description = "ID de la bodega donde se registra el inventario", example = "1")
        @NotNull(message = "El ID de la bodega no puede estar nulo")
        @Positive(message = "El ID de la bodega debe ser positivo")
        Integer bodegaId,

        @Schema(description = "ID del producto registrado en el inventario", example = "1")
        @NotNull(message = "El ID del producto no puede estar nulo")
        @Positive(message = "El ID del producto debe ser positivo")
        Integer productoId,

        @Schema(description = "Stock actual del producto en la bodega", example = "150")
        @NotNull(message = "El stock no puede estar nulo")
        @PositiveOrZero(message = "El stock no puede ser negativo")
        Integer stock
) {}
