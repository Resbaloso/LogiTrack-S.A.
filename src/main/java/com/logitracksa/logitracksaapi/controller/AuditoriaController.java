package com.logitracksa.logitracksaapi.controller;

import com.logitracksa.logitracksaapi.dto.request.AuditoriaDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.AuditoriaDtoResponse;
import com.logitracksa.logitracksaapi.service.AuditoriaService;
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
@Tag(name = "Administrador de auditoría", description = "CRUD de registros de auditoría del sistema")
@RestController
@RequestMapping("/api/auditoria")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    // http://localhost:8080/api/auditoria
    @Operation(summary = "Crea un registro de auditoría", description = "Registra una operación auditada en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Auditoría registrada correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PostMapping
    public ResponseEntity<AuditoriaDtoResponse> guardar(@Valid @RequestBody AuditoriaDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auditoriaService.guardar(dto));
    }

    // http://localhost:8080/api/auditoria
    @Operation(summary = "Lista todas las auditorías", description = "Retorna todos los registros de auditoría")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada correctamente")
    })
    @GetMapping
    public ResponseEntity<List<AuditoriaDtoResponse>> listar() {
        return ResponseEntity.ok(auditoriaService.listar());
    }

    // http://localhost:8080/api/auditoria/1
    @Operation(summary = "Busca una auditoría por ID", description = "Retorna el registro de auditoría con el ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Auditoría encontrada correctamente"),
            @ApiResponse(responseCode = "400", description = "No existe una auditoría con ese ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuditoriaDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(auditoriaService.buscarId(id));
    }

    // http://localhost:8080/api/auditoria/usuario/2
    @Operation(summary = "Busca auditorías por usuario", description = "Retorna las auditorías realizadas por el usuario indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Auditorías encontradas correctamente")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(auditoriaService.buscarUsuario(usuarioId));
    }

    // http://localhost:8080/api/auditoria/filtroTipo?tipoOperacion=INSERT
    @Operation(summary = "Busca auditorías por tipo de operación", description = "Retorna las auditorías del tipo indicado: INSERT, UPDATE o DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Auditorías encontradas correctamente")
    })
    @GetMapping("/filtroTipo")
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarTipoOperacion(@RequestParam String tipoOperacion) {
        return ResponseEntity.ok(auditoriaService.buscarTipoOperacion(tipoOperacion));
    }

    // http://localhost:8080/api/auditoria/filtroEntidad?entidadAfectada=Producto
    @Operation(summary = "Busca auditorías por entidad afectada", description = "Retorna las auditorías que afectaron a la entidad indicada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Auditorías encontradas correctamente")
    })
    @GetMapping("/filtroEntidad")
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarEntidad(@RequestParam String entidadAfectada) {
        return ResponseEntity.ok(auditoriaService.buscarEntidadAfectada(entidadAfectada));
    }

    // Ejemplo: /api/auditoria/filtroFechas?fechaInicio=2026-01-01&fechaFin=2026-11-15
    @Operation(summary = "Busca auditorías por rango de fechas", description = "Retorna las auditorías registradas entre las fechas indicadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Auditorías encontradas correctamente")
    })
    @GetMapping("/filtroFechas")
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return ResponseEntity.ok(auditoriaService.buscarRangoFechas(fechaInicio, fechaFin));
    }

    // http://localhost:8080/api/auditoria/filtroUsuarioTipo?usuarioId=2&tipoOperacion=UPDATE
    @Operation(summary = "Busca auditorías por usuario y tipo", description = "Retorna las auditorías de un usuario filtradas por tipo de operación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Auditorías encontradas correctamente")
    })
    @GetMapping("/filtroUsuarioTipo")
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarUsuarioYTipo(
            @RequestParam Long usuarioId, @RequestParam String tipoOperacion) {
        return ResponseEntity.ok(auditoriaService.buscarUsuarioYTipo(usuarioId, tipoOperacion));
    }

    // http://localhost:8080/api/auditoria/1
    @Operation(summary = "Actualiza una auditoría por ID", description = "Actualiza el registro de auditoría con el ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Auditoría actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "No existe una auditoría con ese ID")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AuditoriaDtoResponse> actualizar(@Valid @RequestBody AuditoriaDtoRequest dto, @PathVariable Long id) {
        return ResponseEntity.ok(auditoriaService.actualizar(dto, id));
    }

    // http://localhost:8080/api/auditoria/1
    @Operation(summary = "Elimina una auditoría por ID", description = "Elimina el registro de auditoría con el ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Auditoría eliminada correctamente"),
            @ApiResponse(responseCode = "400", description = "No existe una auditoría con ese ID")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        auditoriaService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
