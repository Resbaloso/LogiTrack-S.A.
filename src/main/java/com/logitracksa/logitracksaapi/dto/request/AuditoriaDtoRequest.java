package com.logitracksa.logitracksaapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record AuditoriaDtoRequest(
        @Schema(description = "Tipo de operación auditada: INSERT, UPDATE o DELETE", example = "INSERT")
        @NotBlank(message = "El tipo de operación no puede estar vacío")
        String tipoOperacion,

        @Schema(description = "Fecha en que ocurrió la operación", example = "2026-11-15")
        @NotNull(message = "La fecha no puede estar nula")
        LocalDate fecha,

        @Schema(description = "ID del usuario que realizó la operación", example = "1")
        @NotNull(message = "El ID del usuario no puede estar nulo")
        @Positive(message = "El ID del usuario debe ser positivo")
        Integer usuarioId,

        @Schema(description = "Nombre de la entidad que fue afectada por la operación", example = "Bodega")
        @NotBlank(message = "La entidad afectada no puede estar vacía")
        String entidadAfectada,

        @Schema(description = "Valores del registro antes de la operación (puede ser nulo en INSERT)", example = "{\"nombre\": \"Bodega Norte\"}")
        String valoresAnteriores,

        @Schema(description = "Valores del registro después de la operación (puede ser nulo en DELETE)", example = "{\"nombre\": \"Bodega Sur\"}")
        String valoresNuevos
) {}
