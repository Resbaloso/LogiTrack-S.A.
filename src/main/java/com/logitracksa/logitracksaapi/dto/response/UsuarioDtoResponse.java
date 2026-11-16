package com.logitracksa.logitracksaapi.dto.response;

public record UsuarioDtoResponse(
        Long id,
        String nombre,
        String apellidos,
        String email,
        boolean activo
) {}
