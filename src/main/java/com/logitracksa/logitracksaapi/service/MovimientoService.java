package com.logitracksa.logitracksaapi.service;

import com.logitracksa.logitracksaapi.dto.request.MovimientoDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.MovimientoDtoResponse;

import java.time.LocalDate;
import java.util.List;

public interface MovimientoService {

    MovimientoDtoResponse guardar(MovimientoDtoRequest dto);

    MovimientoDtoResponse actualizar(MovimientoDtoRequest dto, Integer id);

    void eliminar(Integer id);

    List<MovimientoDtoResponse> listar();

    MovimientoDtoResponse buscarId(Integer id);

    List<MovimientoDtoResponse> buscarUsuario(Integer usuarioId);

    List<MovimientoDtoResponse> buscarTipo(String tipoMovimiento);

    // Movimientos por rango de fechas
    List<MovimientoDtoResponse> buscarRangoFechas(LocalDate fechaInicio, LocalDate fechaFin);

    List<MovimientoDtoResponse> buscarBodegaOrigen(Integer bodegaOrigenId);

    List<MovimientoDtoResponse> buscarBodegaDestino(Integer bodegaDestinoId);

    List<MovimientoDtoResponse> buscarUsuarioYTipo(Integer usuarioId, String tipoMovimiento);
}
