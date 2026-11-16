package com.logitracksa.logitracksaapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RolDtoRequest(
        @Schema(description = "Nombre del rol del sistema", example = "ADMIN")
        @NotBlank(message = "El nombre del rol no puede estar vacío")
        @Size(min = 2, max = 50, message = "El nombre del rol debe tener entre 2 y 50 caracteres")
        String nombre
) {}
