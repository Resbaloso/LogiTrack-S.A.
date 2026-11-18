package com.logitracksa.logitracksaapi.controller;

import com.logitracksa.logitracksaapi.dto.request.MovimientoDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.MovimientoDtoResponse;
import com.logitracksa.logitracksaapi.service.MovimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Validated
@Tag(name = "Administrador de movimientos", description = "CRUD de movimientos de inventario entre bodegas")
@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    // http://localhost:8080/api/movimientos
    @Operation(summary = "Crea un movimiento", description = "Registra una entrada, salida o transferencia de inventario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Movimiento creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PostMapping
    public ResponseEntity<MovimientoDtoResponse> guardar(@Valid @RequestBody MovimientoDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movimientoService.guardar(dto));
    }

    // http://localhost:8080/api/movimientos
    @Operation(summary = "Lista todos los movimientos", description = "Retorna todos los movimientos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada correctamente")
    })
    @GetMapping
    public ResponseEntity<List<MovimientoDtoResponse>> listar() {
        return ResponseEntity.ok(movimientoService.listar());
    }

    // http://localhost:8080/api/movimientos/1
    @Operation(summary = "Busca un movimiento por ID", description = "Retorna el movimiento que coincida con el ID proporcionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado correctamente"),
            @ApiResponse(responseCode = "400", description = "No existe un movimiento con ese ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.buscarId(id));
    }

    // http://localhost:8080/api/movimientos/usuario/2
    @Operation(summary = "Busca movimientos por usuario", description = "Retorna los movimientos realizados por el usuario indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimientos encontrados correctamente")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<MovimientoDtoResponse>> buscarUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(movimientoService.buscarUsuario(usuarioId));
    }

    // http://localhost:8080/api/movimientos/filtroTipo?tipoMovimiento=ENTRADA
    @Operation(summary = "Busca movimientos por tipo", description = "Retorna los movimientos del tipo indicado: ENTRADA, SALIDA o TRANSFERENCIA")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimientos encontrados correctamente")
    })
    @GetMapping("/filtroTipo")
    public ResponseEntity<List<MovimientoDtoResponse>> buscarTipo(@RequestParam String tipoMovimiento) {
        return ResponseEntity.ok(movimientoService.buscarTipo(tipoMovimiento));
    }

    // Ejemplo: /api/movimientos/filtroFechas?fechaInicio=2026-01-01&fechaFin=2026-11-15
    @Operation(summary = "Busca movimientos por rango de fechas", description = "Retorna los movimientos realizados entre las fechas indicadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimientos encontrados correctamente")
    })
    @GetMapping("/filtroFechas")
    public ResponseEntity<List<MovimientoDtoResponse>> buscarRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return ResponseEntity.ok(movimientoService.buscarRangoFechas(fechaInicio, fechaFin));
    }

    // http://localhost:8080/api/movimientos/bodegaOrigen/1
    @Operation(summary = "Busca movimientos por bodega origen", description = "Retorna los movimientos que salieron de la bodega indicada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimientos encontrados correctamente")
    })
    @GetMapping("/bodegaOrigen/{bodegaOrigenId}")
    public ResponseEntity<List<MovimientoDtoResponse>> buscarBodegaOrigen(@PathVariable Long bodegaOrigenId) {
        return ResponseEntity.ok(movimientoService.buscarBodegaOrigen(bodegaOrigenId));
    }

    // http://localhost:8080/api/movimientos/bodegaDestino/2
    @Operation(summary = "Busca movimientos por bodega destino", description = "Retorna los movimientos que llegaron a la bodega indicada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimientos encontrados correctamente")
    })
    @GetMapping("/bodegaDestino/{bodegaDestinoId}")
    public ResponseEntity<List<MovimientoDtoResponse>> buscarBodegaDestino(@PathVariable Long bodegaDestinoId) {
        return ResponseEntity.ok(movimientoService.buscarBodegaDestino(bodegaDestinoId));
    }

    // http://localhost:8080/api/movimientos/filtroUsuarioTipo?usuarioId=2&tipoMovimiento=SALIDA
    @Operation(summary = "Busca movimientos por usuario y tipo", description = "Retorna los movimientos de un usuario filtrados por tipo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimientos encontrados correctamente")
    })
    @GetMapping("/filtroUsuarioTipo")
    public ResponseEntity<List<MovimientoDtoResponse>> buscarUsuarioYTipo(
            @RequestParam Long usuarioId, @RequestParam String tipoMovimiento) {
        return ResponseEntity.ok(movimientoService.buscarUsuarioYTipo(usuarioId, tipoMovimiento));
    }

    // http://localhost:8080/api/movimientos/1
    @Operation(summary = "Actualiza un movimiento por ID", description = "Actualiza los datos del movimiento con el ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimiento actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "No existe un movimiento con ese ID")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MovimientoDtoResponse> actualizar(@Valid @RequestBody MovimientoDtoRequest dto, @PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.actualizar(dto, id));
    }

    // http://localhost:8080/api/movimientos/1
    @Operation(summary = "Elimina un movimiento por ID", description = "Elimina el movimiento con el ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Movimiento eliminado correctamente"),
            @ApiResponse(responseCode = "400", description = "No existe el movimiento o tiene detalles asociados")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        movimientoService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
