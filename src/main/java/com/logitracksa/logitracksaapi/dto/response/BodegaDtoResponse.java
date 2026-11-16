package com.logitracksa.logitracksaapi.dto.response;

public record BodegaDtoResponse(
        Long id,
        String nombre,
        String ubicacion,
        Integer capacidad,
        UsuarioDtoResponse encargado
) {}
