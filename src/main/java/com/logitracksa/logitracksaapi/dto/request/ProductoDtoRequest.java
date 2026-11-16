package com.logitracksa.logitracksaapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductoDtoRequest(
        @Schema(description = "Nombre del producto", example = "Pallet de madera")
        @NotBlank(message = "El nombre del producto no puede estar vacío")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String nombre,

        @Schema(description = "Descripción detallada del producto", example = "Pallet estándar europeo 1200x800mm")
        @NotBlank(message = "La descripción no puede estar vacía")
        @Size(min = 2, max = 255, message = "La descripción debe tener entre 2 y 255 caracteres")
        String descripcion,

        @Schema(description = "Categoría a la que pertenece el producto", example = "Materiales de embalaje")
        @NotBlank(message = "La categoría no puede estar vacía")
        @Size(min = 2, max = 50, message = "La categoría debe tener entre 2 y 50 caracteres")
        String categoria,

        @Schema(description = "Precio unitario del producto", example = "35000.00")
        @NotNull(message = "El precio no puede estar nulo")
        @Positive(message = "El precio debe ser un valor positivo")
        BigDecimal precio
) {}
