package com.logitracksa.logitracksaapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioDtoRequest(
        @Schema(description = "Nombre del usuario", example = "Carlos")
        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String nombre,

        @Schema(description = "Apellidos del usuario", example = "García López")
        @NotBlank(message = "Los apellidos no pueden estar vacíos")
        @Size(min = 2, max = 100, message = "Los apellidos deben tener entre 2 y 100 caracteres")
        String apellidos,

        @Schema(description = "Correo electrónico del usuario", example = "carlos.garcia@logitracksa.com")
        @NotBlank(message = "El email no puede estar vacío")
        @Email(message = "El email debe tener un formato válido")
        String email,

        @Schema(description = "Indica si el usuario está activo en el sistema", example = "true")
        boolean activo
) {}
