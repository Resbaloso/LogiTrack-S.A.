package com.logitracksa.logitracksaapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CredencialesDtoRequest(
        @Schema(description = "Nombre de usuario para autenticación", example = "carlos.garcia")
        @NotBlank(message = "El username no puede estar vacío")
        @Size(min = 4, max = 50, message = "El username debe tener entre 4 y 50 caracteres")
        String username,

        @Schema(description = "Contraseña del usuario", example = "Str0ng#Pass")
        @NotBlank(message = "La contraseña no puede estar vacía")
        @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
        String password,

        @Schema(description = "ID del usuario al que pertenecen las credenciales", example = "1")
        @NotNull(message = "El ID del usuario no puede estar nulo")
        @Positive(message = "El ID del usuario debe ser positivo")
        Long usuarioId,

        @Schema(description = "ID del rol asignado al usuario", example = "1")
        @NotNull(message = "El ID del rol no puede estar nulo")
        @Positive(message = "El ID del rol debe ser positivo")
        Long rolId
) {}
