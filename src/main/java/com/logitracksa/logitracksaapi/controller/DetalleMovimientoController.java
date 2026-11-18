package com.logitracksa.logitracksaapi.controller;

import com.logitracksa.logitracksaapi.dto.request.DetalleMovimientoDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.DetalleMovimientoDtoResponse;
import com.logitracksa.logitracksaapi.service.DetalleMovimientoService;
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
@Tag(name = "Administrador de detalles de movimiento", description = "CRUD de los productos incluidos en cada movimiento")
@RestController
@RequestMapping("/api/detallemovimiento")
@RequiredArgsConstructor
public class DetalleMovimientoController {

    private final DetalleMovimientoService detalleMovimientoService;

    // http://localhost:8080/api/detallemovimiento
    @Operation(summary = "Crea un detalle de movimiento", description = "Registra un producto y su cantidad dentro de un movimiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Detalle creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PostMapping
    public ResponseEntity<DetalleMovimientoDtoResponse> guardar(@Valid @RequestBody DetalleMovimientoDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(detalleMovimientoService.guardar(dto));
    }

    // http://localhost:8080/api/detallemovimiento
    @Operation(summary = "Lista todos los detalles de movimiento", description = "Retorna todos los detalles registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada correctamente")
    })
    @GetMapping
    public ResponseEntity<List<DetalleMovimientoDtoResponse>> listar() {
        return ResponseEntity.ok(detalleMovimientoService.listar());
    }

    // http://localhost:8080/api/detallemovimiento/1
    @Operation(summary = "Busca un detalle por ID", description = "Retorna el detalle de movimiento con el ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalle encontrado correctamente"),
            @ApiResponse(responseCode = "400", description = "No existe un detalle con ese ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DetalleMovimientoDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(detalleMovimientoService.buscarId(id));
    }

    // http://localhost:8080/api/detallemovimiento/movimiento/3
    @Operation(summary = "Busca detalles por movimiento", description = "Retorna los detalles asociados al movimiento indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalles encontrados correctamente")
    })
    @GetMapping("/movimiento/{movimientoId}")
    public ResponseEntity<List<DetalleMovimientoDtoResponse>> buscarMovimiento(@PathVariable Long movimientoId) {
        return ResponseEntity.ok(detalleMovimientoService.buscarMovimiento(movimientoId));
    }

    // http://localhost:8080/api/detallemovimiento/producto/2
    @Operation(summary = "Busca detalles por producto", description = "Retorna los detalles en los que aparece el producto indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalles encontrados correctamente")
    })
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<DetalleMovimientoDtoResponse>> buscarProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(detalleMovimientoService.buscarProducto(productoId));
    }

    // http://localhost:8080/api/detallemovimiento/movimiento/3/producto/2
    @Operation(summary = "Busca detalles por movimiento y producto", description = "Retorna los detalles que coincidan con el movimiento y producto indicados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalles encontrados correctamente")
    })
    @GetMapping("/movimiento/{movimientoId}/producto/{productoId}")
    public ResponseEntity<List<DetalleMovimientoDtoResponse>> buscarMovimientoYProducto(
            @PathVariable Long movimientoId, @PathVariable Long productoId) {
        return ResponseEntity.ok(detalleMovimientoService.buscarMovimientoYProducto(movimientoId, productoId));
    }

    // http://localhost:8080/api/detallemovimiento/filtroCantidad?cantidad=5
    @Operation(summary = "Busca detalles por cantidad mínima", description = "Retorna los detalles con cantidad mayor o igual al valor indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalles encontrados correctamente")
    })
    @GetMapping("/filtroCantidad")
    public ResponseEntity<List<DetalleMovimientoDtoResponse>> buscarCantidadMinima(@RequestParam int cantidad) {
        return ResponseEntity.ok(detalleMovimientoService.buscarCantidadMinima(cantidad));
    }

    // http://localhost:8080/api/detallemovimiento/1
    @Operation(summary = "Actualiza un detalle por ID", description = "Actualiza el detalle de movimiento con el ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalle actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "No existe un detalle con ese ID")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DetalleMovimientoDtoResponse> actualizar(
            @Valid @RequestBody DetalleMovimientoDtoRequest dto, @PathVariable Long id) {
        return ResponseEntity.ok(detalleMovimientoService.actualizar(dto, id));
    }

    // http://localhost:8080/api/detallemovimiento/1
    @Operation(summary = "Elimina un detalle por ID", description = "Elimina el detalle de movimiento con el ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Detalle eliminado correctamente"),
            @ApiResponse(responseCode = "400", description = "No existe un detalle con ese ID")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        detalleMovimientoService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
