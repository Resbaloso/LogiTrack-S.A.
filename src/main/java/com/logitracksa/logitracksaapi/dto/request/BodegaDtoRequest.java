package com.logitracksa.logitracksaapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record BodegaDtoRequest(
        @Schema(description = "Nombre de la bodega", example = "Bodega Norte")
        @NotBlank(message = "El nombre de la bodega no puede estar vacío")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String nombre,

        @Schema(description = "Ubicación o dirección de la bodega", example = "Carrera 17F #61-10, Bucaramanga")
        @NotBlank(message = "La ubicación no puede estar vacía")
        @Size(min = 2, max = 150, message = "La ubicación debe tener entre 2 y 150 caracteres")
        String ubicacion,

        @Schema(description = "Capacidad máxima de almacenamiento de la bodega", example = "5000")
        @NotNull(message = "La capacidad no puede estar nula")
        @Positive(message = "La capacidad debe ser un valor positivo")
        Integer capacidad,

        @Schema(description = "ID del usuario encargado de la bodega", example = "1")
        @NotNull(message = "El ID del encargado no puede estar nulo")
        @Positive(message = "El ID del encargado debe ser positivo")
        Integer usuarioId
) {}
