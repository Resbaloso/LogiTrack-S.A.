package com.logitracksa.logitracksaapi.service;

import com.logitracksa.logitracksaapi.dto.request.MovimientoDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.MovimientoDtoResponse;

import java.time.LocalDate;
import java.util.List;

public interface MovimientoService {

    MovimientoDtoResponse guardar(MovimientoDtoRequest dto);

    MovimientoDtoResponse actualizar(MovimientoDtoRequest dto, Long id);

    void eliminar(Long id);

    List<MovimientoDtoResponse> listar();

    MovimientoDtoResponse buscarId(Long id);

    List<MovimientoDtoResponse> buscarUsuario(Long usuarioId);

    List<MovimientoDtoResponse> buscarTipo(String tipoMovimiento);

    // Movimientos entre un rango de fechas
    List<MovimientoDtoResponse> buscarRangoFechas(LocalDate fechaInicio, LocalDate fechaFin);

    List<MovimientoDtoResponse> buscarBodegaOrigen(Long bodegaOrigenId);

    List<MovimientoDtoResponse> buscarBodegaDestino(Long bodegaDestinoId);

    List<MovimientoDtoResponse> buscarUsuarioYTipo(Long usuarioId, String tipoMovimiento);
}
