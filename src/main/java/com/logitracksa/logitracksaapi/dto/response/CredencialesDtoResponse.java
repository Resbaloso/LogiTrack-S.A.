package com.logitracksa.logitracksaapi.dto.response;

public record CredencialesDtoResponse(
        Long id,
        String username,
        UsuarioDtoResponse usuario,
        RolDtoResponse rol
) {}
