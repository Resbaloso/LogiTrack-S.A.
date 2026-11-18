package com.logitracksa.logitracksaapi.controller;

import com.logitracksa.logitracksaapi.dto.request.InventarioDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.InventarioDtoResponse;
import com.logitracksa.logitracksaapi.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@Tag(name = "Administrador de inventario", description = "CRUD de inventario por bodega")
@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    // http://localhost:8080/api/inventario
    @Operation(summary = "Crea un registro de inventario", description = "Registra el stock de un producto en una bodega")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inventario registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PostMapping
    public ResponseEntity<InventarioDtoResponse> guardar(@Valid @RequestBody InventarioDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.guardar(dto));
    }

    // http://localhost:8080/api/inventario
    @Operation(summary = "Lista todo el inventario", description = "Retorna todos los registros de inventario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada correctamente")
    })
    @GetMapping
    public ResponseEntity<List<InventarioDtoResponse>> listar() {
        return ResponseEntity.ok(inventarioService.listar());
    }

    // http://localhost:8080/api/inventario/1
    @Operation(summary = "Busca un inventario por ID", description = "Retorna el registro de inventario con el ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventario encontrado correctamente"),
            @ApiResponse(responseCode = "400", description = "No existe un inventario con ese ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<InventarioDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.buscarId(id));
    }

    // http://localhost:8080/api/inventario/bodega/1
    @Operation(summary = "Busca inventario por bodega", description = "Retorna todos los productos registrados en la bodega indicada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventario encontrado correctamente")
    })
    @GetMapping("/bodega/{bodegaId}")
    public ResponseEntity<List<InventarioDtoResponse>> buscarBodega(@PathVariable Long bodegaId) {
        return ResponseEntity.ok(inventarioService.buscarBodega(bodegaId));
    }

    // http://localhost:8080/api/inventario/producto/1
    @Operation(summary = "Busca inventario por producto", description = "Retorna todas las bodegas que tienen el producto indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventario encontrado correctamente")
    })
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<InventarioDtoResponse>> buscarProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.buscarProducto(productoId));
    }

    // http://localhost:8080/api/inventario/bodega/1/producto/2
    @Operation(summary = "Busca inventario por bodega y producto", description = "Retorna el stock de un producto específico en una bodega específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventario encontrado correctamente")
    })
    @GetMapping("/bodega/{bodegaId}/producto/{productoId}")
    public ResponseEntity<List<InventarioDtoResponse>> buscarBodegaYProducto(
            @PathVariable Long bodegaId, @PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.buscarBodegaYProducto(bodegaId, productoId));
    }

    // http://localhost:8080/api/inventario/stockBajo?umbral=10
    @Operation(summary = "Busca productos con stock bajo", description = "Retorna los registros de inventario cuyo stock sea menor al umbral indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventario encontrado correctamente")
    })
    @GetMapping("/stockBajo")
    public ResponseEntity<List<InventarioDtoResponse>> buscarStockBajo(@RequestParam int umbral) {
        return ResponseEntity.ok(inventarioService.buscarStockBajo(umbral));
    }

    // http://localhost:8080/api/inventario/1
    @Operation(summary = "Actualiza un inventario por ID", description = "Actualiza el registro de inventario con el ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventario actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "No existe un inventario con ese ID")
    })
    @PutMapping("/{id}")
    public ResponseEntity<InventarioDtoResponse> actualizar(@Valid @RequestBody InventarioDtoRequest dto, @PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.actualizar(dto, id));
    }

    // http://localhost:8080/api/inventario/1
    @Operation(summary = "Elimina un inventario por ID", description = "Elimina el registro de inventario con el ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Inventario eliminado correctamente"),
            @ApiResponse(responseCode = "400", description = "No existe un inventario con ese ID")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        inventarioService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
